package org.flexpay.ab.dao.impl;

import org.flexpay.ab.dao.RegionDaoExt;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.sorter.RegionSorter;
import org.flexpay.ab.persistence.sorter.RegionSorterStub;
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

public class RegionDaoExtImpl extends HibernateDaoSupport implements RegionDaoExt {

	/**
	 * Find and sort regions
	 *
	 * @param countryId country key
	 * @param sorters   Collection of sorters
	 * @param pager	 Pager
	 * @return List of regions
	 */
	@SuppressWarnings ({"unchecked"})
	@NotNull
	@Override
	public List<Region> findRegions(Long countryId, Collection<? extends ObjectSorter> sorters, final Page<Region> pager) {
		RegionSorter sorter = findSorter(sorters);
		sorter.setRegionField("o");

		final StringBuilder cnthql = new StringBuilder();
		final StringBuilder hql = new StringBuilder();

		cnthql.append("select count(o) from Region o ");
		hql.append("select distinct o from Region o ");
		sorter.setFrom(hql);

		StringBuilder whereClause = new StringBuilder();
		whereClause.append(" where o.parent.id=").append(countryId).append(" and o.status=").append(Region.STATUS_ACTIVE);
		sorter.setWhere(whereClause);
		hql.append(whereClause);
		cnthql.append(" where o.parent.id=").append(countryId).append(" and o.status=").append(Region.STATUS_ACTIVE);

		StringBuilder orderByClause = new StringBuilder();
		sorter.setOrderBy(orderByClause);
		if (orderByClause.length() > 0) {
			hql.append(" ORDER BY ").append(orderByClause);
		}

		return getHibernateTemplate().executeFind(new HibernateCallback<List<?>>() {
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
	private RegionSorter findSorter(Collection<? extends ObjectSorter> sorters) {

		for (ObjectSorter sorter : sorters) {
			if (sorter.isActivated() && sorter instanceof RegionSorter) {
				return (RegionSorter) sorter;
			}
		}

		return new RegionSorterStub();
	}

}
