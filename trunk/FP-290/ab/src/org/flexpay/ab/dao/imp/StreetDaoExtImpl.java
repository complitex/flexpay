package org.flexpay.ab.dao.imp;

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
import java.util.Date;
import java.util.List;

public class StreetDaoExtImpl extends HibernateDaoSupport implements StreetDaoExt {

	public void invalidateTypeTemporals(final Long streetId, final Date futureInfinity, final Date invalidDate) {

		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				return session.createQuery("update StreetTypeTemporal t set t.invalidDate = :invalidDate " +
										   "where t.object.id = :streetId and t.invalidDate=:futureInfinity")
						.setDate("invalidDate", invalidDate)
						.setLong("streetId", streetId)
						.setDate("futureInfinity", futureInfinity)
						.executeUpdate();
			}
		});
	}

	/**
	 * Check if street is in a town
	 *
	 * @param townId   Town key to check in
	 * @param streetId Street key to check
	 * @return <code>true</code> if requested street is in a town
	 */
	public boolean isStreetInTown(Long townId, Long streetId) {
		Object[] params = {townId, streetId};
		return !getHibernateTemplate().find("from Street where parent.id=? and id=?", params).isEmpty();
	}

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

		StringBuilder orderByClause = new StringBuilder();
		sorter.setOrderBy(orderByClause);
		if (orderByClause.length() > 0) {
			hql.append(" ORDER BY ").append(orderByClause);
		}

		return getHibernateTemplate().executeFind(new HibernateCallback() {
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
}
