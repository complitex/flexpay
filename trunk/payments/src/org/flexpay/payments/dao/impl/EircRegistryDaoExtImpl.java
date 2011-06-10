package org.flexpay.payments.dao.impl;

import org.flexpay.common.dao.FilterHandler;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.RegistryTypeFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.sorter.RegistrySorter;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.flexpay.payments.dao.EircRegistryDaoExt;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;

public class EircRegistryDaoExtImpl extends JpaDaoSupport implements EircRegistryDaoExt {

	private Logger log = LoggerFactory.getLogger(getClass());

	private List<FilterHandler> filterHandlers = FilterHandlers.registryFilterHandlers();

	/**
	 * Find registries
	 *
     * @param registrySorter registry sorter
	 * @param filters ObjectFilters
	 * @param pager   Page
	 * @return list of registries matching specified criteria
	 */
	@Override
	public List<Registry> findRegistries(RegistrySorter registrySorter, Collection<ObjectFilter> filters, final Page<?> pager) {

		final List<Object> params = list();
		final StringBuilder hql = new StringBuilder("select distinct r from Registry r ")
				.append("left join fetch r.properties rps ")
				.append("left join fetch r.registryType rt ")
				.append("left join fetch r.registryStatus rs ")
				.append("left join fetch r.archiveStatus ras, ")
				.append("EircRegistryProperties rp ")
				.append("left join rp.sender sender ")
				.append("left join rp.recipient recipient ")
                .append("left join rp.serviceProvider serviceProvider ")
				.append("where rps.id=rp.id ");
		final StringBuilder hqlCount = new StringBuilder("select count(r.id) from Registry r ")
				.append("left join r.properties rps ")
				.append("left join r.registryType rt ")
				.append("left join r.registryStatus rs, ")
				.append("EircRegistryProperties rp ")
				.append("left join rp.sender sender ")
				.append("left join rp.recipient recipient ")
                .append("left join rp.serviceProvider serviceProvider ")
				.append("where rps.id=rp.id ");

		for (ObjectFilter filter : filters) {
			if (!filter.needFilter()) {
				continue;
			}
			for (FilterHandler handler : filterHandlers) {
				if (handler.supports(filter)) {
					hql.append(" and ");
					hqlCount.append(" and ");
					List<?> parameters = handler.whereClause(filter, hqlCount);
					handler.whereClause(filter, hql);
					params.addAll(parameters);
				}
			}
		}

        hql.append(" order by ");

		if (registrySorter != null) {
			StringBuilder orderByClause = new StringBuilder();
			registrySorter.setOrderBy(orderByClause);

			if (orderByClause.length() > 0) {
				hql.append(orderByClause).append(",");
			}
		}
        hql.append("r.id desc");

		log.debug("Registries list queries: \n{}\n{}", hql, hqlCount);

		// retrieve elements
		@SuppressWarnings ({"unchecked"})
		List<Registry> result = getJpaTemplate().executeFind(new JpaCallback() {
			@Override
			public Object doInJpa(EntityManager entityManager) throws HibernateException {
				javax.persistence.Query qCount = entityManager.createQuery(hqlCount.toString());
				javax.persistence.Query query = entityManager.createQuery(hql.toString());
				for (int n = 0; n < params.size(); ++n) {
					qCount.setParameter(n + 1, params.get(n));
					query.setParameter(n + 1, params.get(n));
				}

				Number objectsCount = (Number) qCount.getSingleResult();
				pager.setTotalElements(objectsCount.intValue());

				return query.setFirstResult(pager.getThisPageFirstElementNumber())
						.setMaxResults(pager.getPageSize()).getResultList();
			}
		});

		setRegistryFiles(result);

		return result;
	}

	/**
	 * Find registries
	 *
	 * @param senderFilter	sender organization filter
	 * @param recipientFilter recipient organization filter
	 * @param typeFilter	  registry type filter
	 * @param fromDate		registry generation start date
	 * @param tillDate		registry generation end date
	 * @param pager		   Page
	 * @return list of registries matching specified criteria
	 */
	@SuppressWarnings ({"unchecked"})
	public List<Registry> findRegistries(
			OrganizationFilter senderFilter, OrganizationFilter recipientFilter,
			RegistryTypeFilter typeFilter, Date fromDate, Date tillDate, final Page<?> pager) {

		return null;
	}

	private void setRegistryFiles(List<Registry> registries) {

		if (registries.isEmpty()) {
			return;
		}

		final Collection<Long> ids = DomainObject.collectionIds(registries);
		@SuppressWarnings ({"unchecked"})
		List<Registry> rFiles = getJpaTemplate().executeFind(new JpaCallback() {
			@Override
			public Object doInJpa(EntityManager entityManager) throws HibernateException {
				return entityManager.createNamedQuery("Registry.getRegistriesFiles")
						.setParameter("ids", ids)
						.getResultList();
			}
		});

		Map<Long, Registry> id2Registry = CollectionUtils.map(
				rFiles, DomainObject.<Registry>idExtractor());

		// copy fetched files to registries
		for (Registry registry : registries) {
			Registry rFilesHolder = id2Registry.get(registry.getId());
			if (rFilesHolder != null) {
				registry.setFiles(rFilesHolder.getFiles());
			}
		}
	}

	@Override
	public void deleteQuittances(final Long registryId) {
		getJpaTemplate().execute(new JpaCallback<Void>() {
			@Override
			public Void doInJpa(EntityManager entityManager) throws HibernateException {
				entityManager.createNamedQuery("Registry.deleteQuittances")
						.setParameter(1, registryId).executeUpdate();
				return null;
			}
		});
	}
}
