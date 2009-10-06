package org.flexpay.ab.dao.impl;

import org.flexpay.ab.dao.StreetDaoExt;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.sorter.StreetSorter;
import org.flexpay.ab.persistence.sorter.StreetSorterStub;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collection;
import java.util.List;

public class StreetDaoExtImpl extends HibernateDaoSupport implements StreetDaoExt {

	/**
	 * Find and sort streets
	 *
	 * @param townId  Town key
	 * @param sorters Collection of sorters
	 * @param pager   Pager
	 * @return List of streets
	 */
	@SuppressWarnings ({"unchecked"})
	@NotNull
	@Override
	public List<Street> findStreets(Long townId, Collection<ObjectSorter> sorters, final Page<Street> pager) {

		StreetSorter sorter = findSorter(sorters);
		sorter.setStreetField("s");

		final StringBuilder cnthql = new StringBuilder();
		final StringBuilder hql = new StringBuilder();

		cnthql.append("select count(s) from Street s ");
		hql.append("select distinct s from Street s ");
		sorter.setFrom(hql);

		StringBuilder whereClause = new StringBuilder();
		whereClause.append(" where s.parent.id=").append(townId).append(" and s.status=").append(Street.STATUS_ACTIVE);
		sorter.setWhere(whereClause);
		hql.append(whereClause);
		cnthql.append(" where s.parent.id=").append(townId).append(" and s.status=").append(Street.STATUS_ACTIVE);

		StringBuilder orderByClause = new StringBuilder();
		sorter.setOrderBy(orderByClause);
		if (orderByClause.length() > 0) {
			hql.append(" ORDER BY ").append(orderByClause);
		}

		return getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public List<?> doInHibernate(Session session) throws HibernateException {
				Query cntQuery = session.createQuery(cnthql.toString());
				Long count = (Long) cntQuery.uniqueResult();
				pager.setTotalElements(count.intValue());

				return session.createQuery(hql.toString())
						.setFirstResult(pager.getThisPageFirstElementNumber())
						.setMaxResults(pager.getPageSize())
						.list();

			}
		});
	}

	@SuppressWarnings ({"unchecked"})
	@NotNull
	@Override
	public List<Street> findByTownAndQuery(Long townId, Collection<ObjectSorter> sorters, String query, final Page<Street> pager) {
		StreetSorter sorter = findSorter(sorters);
		sorter.setStreetField("s");

		final StringBuilder cnthql = new StringBuilder();
		final StringBuilder hql = new StringBuilder();

		cnthql.append("select count(s) from Street s ").
				append(" left join s.nameTemporals t").
				append(" left join t.value.translations tr");
		hql.append("select distinct s from Street s ").
				append(" left join fetch s.nameTemporals t").
				append(" left join fetch t.value v").
				append(" left join fetch v.translations tr");
		sorter.setFrom(hql);

		StringBuilder whereClause = new StringBuilder();
		whereClause.append(" where s.parent.id=").append(townId).append(" and s.status=").append(Street.STATUS_ACTIVE).
				append(" and lower(tr.name) like '").append(query).append("'");
		sorter.setWhere(whereClause);
		hql.append(whereClause);
		cnthql.append(" where s.parent.id=").append(townId).append(" and s.status=").append(Street.STATUS_ACTIVE).
				append(" and lower(tr.name) like '").append(query).append("'");

		StringBuilder orderByClause = new StringBuilder();
		sorter.setOrderBy(orderByClause);
		if (orderByClause.length() > 0) {
			hql.append(" ORDER BY ").append(orderByClause);
		}

		return getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public List<?> doInHibernate(Session session) throws HibernateException {
				Query cntQuery = session.createQuery(cnthql.toString());
				Long count = (Long) cntQuery.uniqueResult();
				pager.setTotalElements(count.intValue());

				return session.createQuery(hql.toString())
						.setFirstResult(pager.getThisPageFirstElementNumber())
						.setMaxResults(pager.getPageSize())
						.list();

			}
		});
	}

	@NotNull
	private StreetSorter findSorter(Collection<ObjectSorter> sorters) {

		for (ObjectSorter sorter : sorters) {
			if (sorter.isActivated() && sorter instanceof StreetSorter) {
				return (StreetSorter) sorter;
			}
		}

		return new StreetSorterStub();
	}

	@Override
	public void deleteStreet(final Long streetId) {
		getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				session.getNamedQuery("Street.deleteNameTranslations")
						.setLong(0, streetId).executeUpdate();
				session.getNamedQuery("Street.deleteNameTemporals")
						.setLong(0, streetId).executeUpdate();
				session.getNamedQuery("Street.deleteNames")
						.setLong(0, streetId).executeUpdate();
				session.getNamedQuery("Street.deleteTypeTemporals")
						.setLong(0, streetId).executeUpdate();
				session.getNamedQuery("Street.deleteStreet")
						.setLong(0, streetId).executeUpdate();
				return null;
			}
		});
	}
}
