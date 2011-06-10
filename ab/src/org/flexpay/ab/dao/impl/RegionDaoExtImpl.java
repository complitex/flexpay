package org.flexpay.ab.dao.impl;

import org.flexpay.ab.dao.RegionDaoExt;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.sorter.RegionSorter;
import org.flexpay.ab.persistence.sorter.RegionSorterStub;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.List;

public class RegionDaoExtImpl extends JpaDaoSupport implements RegionDaoExt {

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

		return getJpaTemplate().executeFind(new JpaCallback() {
			@Override
			public List<?> doInJpa(EntityManager entityManager) throws PersistenceException {
				javax.persistence.Query cntQuery = entityManager.createQuery(cnthql.toString());
				Long count = (Long) cntQuery.getSingleResult();
				pager.setTotalElements(count.intValue());

				return entityManager.createQuery(hql.toString())
						.setFirstResult(pager.getThisPageFirstElementNumber())
						.setMaxResults(pager.getPageSize())
						.getResultList();

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
