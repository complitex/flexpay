package org.flexpay.ab.dao.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.dao.TownDaoExt;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.sorter.TownSorter;
import org.flexpay.ab.persistence.sorter.TownSorterStub;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.ObjectWithStatus;
import org.flexpay.common.persistence.filter.StringValueFilter;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.List;

import static org.flexpay.common.persistence.filter.StringValueFilter.TYPE_TOWN;
import static org.flexpay.common.persistence.filter.StringValueFilter.TYPE_TOWN_TYPE;

public class TownDaoExtImpl extends JpaDaoSupport implements TownDaoExt {

    private final Logger log = LoggerFactory.getLogger(getClass());

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
	private TownSorter findSorter(Collection<? extends ObjectSorter> sorters) {

		for (ObjectSorter sorter : sorters) {
			if (sorter.isActivated() && sorter instanceof TownSorter) {
				return (TownSorter) sorter;
			}
		}

		return new TownSorterStub();
	}

/*
    select distinct t
    from Town t
        left join fetch t.nameTemporals nt
        left join fetch nt.value ntv
        left join fetch ntv.translations tr
        left join fetch t.typeTemporals tt
        left join fetch tt.value ttv
        left join fetch ttv.translations ttr
    where t.status=0
          and nt.invalidDate='2100-12-31' and lower(tr.name) like ?
          and tt.invalidDate='2100-12-31' and lower(ttr.name) like ?
*/

    @SuppressWarnings({"unchecked"})
    @NotNull
    @Override
    public Town findTown(@NotNull final ArrayStack filters) {

        final StringBuilder hql = new StringBuilder("SELECT DISTINCT t FROM Town t" +
                "        left join fetch t.nameTemporals nt" +
                "        left join fetch nt.value ntv" +
                "        left join fetch ntv.translations tr" +
                "        left join fetch t.typeTemporals tt" +
                "        left join fetch tt.value ttv" +
                "        left join fetch ttv.translations ttr");
		final StringBuilder filterHql = getFilterHql(filters);

        hql.append(filterHql);

        List<Town> towns = (List<Town>) getJpaTemplate().executeFind(new JpaCallback() {
			@Override
			public List<Town> doInJpa(EntityManager entityManager) throws PersistenceException {

                javax.persistence.Query query = entityManager.createQuery(hql.toString());
                setQueryParameters(query, filters);
                log.debug("Towns search query: {}", query);
                return (List<Town>) query.getResultList();
            }
        });

        if (log.isDebugEnabled()) {
            log.debug("Found {} towns", towns.size());
        }

        return towns.get(0);

    }

    private void setQueryParameters(javax.persistence.Query query, @NotNull ArrayStack filters) {

        for (Object f : filters) {

            StringValueFilter filter = (StringValueFilter) f;

            if (filter.needFilter()) {

                if (TYPE_TOWN_TYPE.equals(filter.getType())) {
                    query.setParameter("townTypeName", filter.getValue().toLowerCase());
                } else if (TYPE_TOWN.equals(filter.getType())) {
                    query.setParameter("townName", filter.getValue().toLowerCase());
                }

            }
        }

    }

    private StringBuilder getFilterHql(@NotNull ArrayStack filters) {

         StringBuilder filterHql = new StringBuilder(" WHERE t.status=").append(ObjectWithStatus.STATUS_ACTIVE);

        for (Object f : filters) {

            StringValueFilter filter = (StringValueFilter) f;

            if (filter.needFilter()) {

                if (TYPE_TOWN_TYPE.equals(filter.getType())) {
                    filterHql.append(" AND nt.invalidDate='2100-12-31' AND lower(ttr.name) like :townTypeName");
                } else if (TYPE_TOWN.equals(filter.getType())) {
                    filterHql.append(" AND tt.invalidDate='2100-12-31' AND lower(tr.name) like :townName");
                }

            }

        }

        return filterHql;

    }

}
