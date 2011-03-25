package org.flexpay.ab.dao.impl;

import org.flexpay.ab.dao.DistrictDaoExt;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.sorter.DistrictSorter;
import org.flexpay.ab.persistence.sorter.DistrictSorterStub;
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

public class DistrictDaoExtImpl extends HibernateDaoSupport implements DistrictDaoExt {

	/**
	 * Find and sort districts
	 *
	 * @param townId Town key
	 * @param sorters Collection of sorters
	 * @param pager Pager
	 * @return List of districts
	 */
	@SuppressWarnings ({"unchecked"})
	@NotNull
	@Override
	public List<District> findDistricts(Long townId, Collection<? extends ObjectSorter> sorters, final Page<District> pager) {
		DistrictSorter sorter = findSorter(sorters);
		sorter.setDistrictField("d");

		final StringBuilder cnthql = new StringBuilder();
		final StringBuilder hql = new StringBuilder();

		cnthql.append("select count(d) from District d ").
                append(" left join d.nameTemporals t ");
		hql.append("select distinct d from District d ").
                append(" left join d.nameTemporals t ");
		sorter.setFrom(hql);

		StringBuilder whereClause = new StringBuilder();
		whereClause.append(" where d.parent.id=").append(townId).append(" and d.status=").append(District.STATUS_ACTIVE).append(" and t.end='2100-12-31'");
		sorter.setWhere(whereClause);
		hql.append(whereClause);
		cnthql.append(" where d.parent.id=").append(townId).append(" and d.status=").append(District.STATUS_ACTIVE).append(" and t.end='2100-12-31'");

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
	private DistrictSorter findSorter(Collection<? extends ObjectSorter> sorters) {

		for (ObjectSorter sorter : sorters) {
			if (sorter.isActivated() && sorter instanceof DistrictSorter) {
				return (DistrictSorter) sorter;
			}
		}

		return new DistrictSorterStub();
	}

}
