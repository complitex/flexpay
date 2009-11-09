package org.flexpay.ab.dao.impl;

import org.flexpay.ab.dao.TownDaoExt;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.sorter.TownSorter;
import org.flexpay.ab.persistence.sorter.TownSorterStub;
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

public class TownDaoExtImpl extends HibernateDaoSupport implements TownDaoExt {

	/**
	 * Find and sort towns
	 *
	 * @param regionId Region key
	 * @param sorters Collection of sorters
	 * @param pager	Pager
	 * @return List of towns
	 */
	@SuppressWarnings ({"unchecked"})
	@NotNull
	@Override
	public List<Town> findTowns(Long regionId, Collection<? extends ObjectSorter> sorters, final Page<Town> pager) {
		TownSorter sorter = findSorter(sorters);
		sorter.setTownField("o");

		final StringBuilder cnthql = new StringBuilder();
		final StringBuilder hql = new StringBuilder();

		cnthql.append("select count(o) from Town o ");
		hql.append("select distinct o from Town o ");
		sorter.setFrom(hql);

		StringBuilder whereClause = new StringBuilder();
		whereClause.append(" where o.parent.id=").append(regionId).append(" and o.status=").append(Town.STATUS_ACTIVE);
		sorter.setWhere(whereClause);
		hql.append(whereClause);
		cnthql.append(" where o.parent.id=").append(regionId).append(" and o.status=").append(Town.STATUS_ACTIVE);

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
	private TownSorter findSorter(Collection<? extends ObjectSorter> sorters) {

		for (ObjectSorter sorter : sorters) {
			if (sorter.isActivated() && sorter instanceof TownSorter) {
				return (TownSorter) sorter;
			}
		}

		return new TownSorterStub();
	}

}
