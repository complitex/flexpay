package org.flexpay.payments.dao.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.dao.registry.impl.RegistryDaoExtImpl;
import org.flexpay.common.persistence.filter.RegistryTypeFilter;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.payments.dao.EircRegistryDaoExt;
import org.flexpay.orgs.persistence.filters.OrganizationFilter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.util.*;

public class EircRegistryDaoExtImpl extends RegistryDaoExtImpl implements EircRegistryDaoExt {

	private Logger log = LoggerFactory.getLogger(getClass());

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

		final List<Object> params = new ArrayList();
		final StringBuilder hql = new StringBuilder("select distinct r from Registry r ")
				.append("left join fetch r.spFile ")
				.append("inner join fetch r.properties rps ")
				.append("inner join fetch r.registryType rt ")
				.append("inner join fetch r.registryStatus rs ")
				.append("inner join fetch r.archiveStatus ras, ")
				.append("EircRegistryProperties rp ")
				.append("where rps.id=rp.id ");
		final StringBuilder hqlCount = new StringBuilder("select count(r) from Registry r ")
				.append("inner join r.properties rps ")
				.append("inner join r.registryType rt ")
				.append("inner join r.registryStatus rs, ")
				.append("EircRegistryProperties rp ")
				.append("inner join rp.sender sender ")
				.append("inner join rp.recipient recipient ")
				.append("where rps.id=rp.id ");

		if (senderFilter.needFilter()) {
			hql.append("and sender.id=? ");
			hqlCount.append("and sender.id=? ");
			params.add(senderFilter.getSelectedId());
		}
		if (recipientFilter.needFilter()) {
			hql.append("and recipient.id=? ");
			hqlCount.append("and recipient.id=? ");
			params.add(recipientFilter.getSelectedId());
		}
		if (typeFilter.needFilter()) {
			hql.append("and rt.id=? ");
			hqlCount.append("and rt.id=? ");
			params.add(typeFilter.getSelectedId());
		}
		if (fromDate != null) {
			hql.append("and r.creationDate >= ? ");
			hqlCount.append("and r.creationDate >= ? ");
			params.add(fromDate);
		}
		if (tillDate != null) {
			hql.append("and r.creationDate <= ? ");
			hqlCount.append("and r.creationDate <= ? ");
			params.add(tillDate);
		}

		// retrive total count of elements
		Number count = (Number) getHibernateTemplate().find(hqlCount.toString(), params.toArray()).get(0);
		pager.setTotalElements(count.intValue());

		log.debug("Registries list queries: \n{}\n{}", hql, hqlCount);

		// retrive elements
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query qCount = session.createQuery(hqlCount.toString());
				Query query = session.createQuery(hql.toString());
				for (int n = 0; n < params.size(); ++n) {
					qCount.setParameter(n, params.get(n));
					query.setParameter(n, params.get(n));
				}

				Number objectsCount = (Number) qCount.uniqueResult();
				pager.setTotalElements(objectsCount.intValue());

				return query.setFirstResult(pager.getThisPageFirstElementNumber())
						.setMaxResults(pager.getPageSize()).list();
			}
		});
	}

	public void deleteQuittances(final Long registryId) {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Void doInHibernate(Session session) throws HibernateException {
				session.getNamedQuery("Registry.deleteQuittances").setLong(1, registryId).executeUpdate();
				return null;
			}
		});
	}
}
