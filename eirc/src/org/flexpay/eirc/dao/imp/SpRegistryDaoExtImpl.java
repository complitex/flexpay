package org.flexpay.eirc.dao.imp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.dao.SpRegistryDaoExt;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.eirc.persistence.filters.OrganisationFilter;
import org.flexpay.eirc.persistence.filters.RegistryTypeFilter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.*;

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
	@SuppressWarnings({"unchecked"})
	public List<SpRegistry> findRegistries(
			OrganisationFilter senderFilter, OrganisationFilter recipientFilter,
			RegistryTypeFilter typeFilter, Date fromDate, Date tillDate, final Page pager) {

		final List params = new ArrayList();
		final StringBuilder hql = new StringBuilder("select distinct r from SpRegistry r ")
				.append("left join fetch r.spFile ")
				.append("inner join fetch r.registryType rt ")
				.append("inner join fetch r.registryStatus rs ")
				.append("inner join fetch r.archiveStatus ras ")
				.append("inner join fetch r.sender sender ")
				.append("left join fetch sender.names ")
				.append("inner join fetch r.recipient recipient ")
				.append("left join fetch recipient.names ")
				.append("left join fetch r.serviceProvider ")
				.append("where 1=1 ");
		final StringBuilder hqlCount = new StringBuilder("select count(*) from SpRegistry r ")
				.append("inner join r.registryType rt ")
				.append("inner join r.registryStatus rs ")
				.append("inner join r.sender sender ")
				.append("inner join r.recipient recipient ")
				.append("where 1=1 ");

		if (senderFilter.getSelectedId() > 0) {
			hql.append("and sender.id=? ");
			hqlCount.append("and sender.id=? ");
			params.add(senderFilter.getSelectedId());
		}
		if (recipientFilter.getSelectedId() > 0) {
			hql.append("and recipient.id=? ");
			hqlCount.append("and recipient.id=? ");
			params.add(recipientFilter.getSelectedId());
		}
		if (typeFilter.getSelectedId() > 0) {
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

		// retrive elements
		List registries = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query qCount = session.createQuery(hqlCount.toString());
				Query query = session.createQuery(hql.toString());
				for (int n = 0; n < params.size(); ++n) {
					qCount.setParameter(n, params.get(n));
					query.setParameter(n, params.get(n));
				}

				Number count = (Number) qCount.uniqueResult();
				pager.setTotalElements(count.intValue());

				return query.setFirstResult(pager.getThisPageFirstElementNumber())
						.setMaxResults(pager.getPageSize()).list();
			}
		});

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
