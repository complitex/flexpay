package org.flexpay.eirc.dao.imp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.dao.SpRegistryDaoExt;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.filters.OrganisationFilter;
import org.flexpay.eirc.persistence.filters.RegistryTypeFilter;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Set;
import java.sql.SQLException;

public class SpRegistryDaoExtImpl extends HibernateDaoSupport implements SpRegistryDaoExt {

	/**
	 * Find registries
	 *
	 * @param senderFilter	sender organisation filter
	 * @param recipientFilter recipient organisation filter
	 * @param typeFilter	  registry type filter
	 * @param fromDate		registry generation start date
	 * @param tillDate		registry generation end date
	 * @param pager		   Page
	 * @return list of registries matching specified criteria
	 */
	@SuppressWarnings ({"unchecked"})
	public List<SpRegistry> findRegistries(
			OrganisationFilter senderFilter, OrganisationFilter recipientFilter,
			RegistryTypeFilter typeFilter, Date fromDate, Date tillDate, Page pager) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SpRegistry.class)
				.setFetchMode("spFile", FetchMode.JOIN)
				.setFetchMode("registryType", FetchMode.JOIN)
				.setFetchMode("registryStatus", FetchMode.JOIN)
				.setFetchMode("sender", FetchMode.JOIN)
				.setFetchMode("recipient", FetchMode.JOIN)
				.setFetchMode("serviceProvider", FetchMode.JOIN);
		if (senderFilter.getSelectedId() > 0) {
			criteria.createAlias("sender", "s")
					.add(Restrictions.eq("s.id", senderFilter.getSelectedId()));
		}
		if (recipientFilter.getSelectedId() > 0) {
			criteria.createAlias("recipient", "r")
					.add(Restrictions.eq("r.id", recipientFilter.getSelectedId()));
		}
		if (typeFilter.getSelectedId() > 0) {
			criteria.createAlias("registryType", "t")
					.add(Restrictions.eq("t.id", typeFilter.getSelectedId()));
		}
		if (fromDate != null) {
			criteria.add(Restrictions.ge("creationDate", fromDate));
		}
		if (tillDate != null) {
			criteria.add(Restrictions.le("creationDate", tillDate));
		}

		// retrive elements
		List registries = getHibernateTemplate().findByCriteria(criteria,
				pager.getThisPageFirstElementNumber(), pager.getPageSize());
		pager.setElements(registries);

		// retrive total count of elements
		DetachedCriteria countCrit = criteria.setProjection(Projections.rowCount());
		Number count = (Number) getHibernateTemplate().findByCriteria(countCrit).get(0);
		pager.setTotalElements(count.intValue());

		return registries;
	}

	/**
	 * Find registries by identifiers
	 *
	 * @param objectIds Set of registry identifiers
	 * @return collection of registries
	 */
	@SuppressWarnings({"unchecked"})
	public Collection<SpRegistry> findRegistries(final Set<Long> objectIds) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createQuery("from SpRegistry r inner join fetch r.serviceProvider sp " +
						"inner join fetch sp.dataSourceDescription inner join fetch r.registryType " +
						"inner join fetch r.registryStatus " +
						"where r.id in (:ids)")
						.setParameterList("ids", objectIds).list();
			}
		});
	}
}
