package org.flexpay.ab.dao.impl;

import org.flexpay.ab.dao.CountryDaoExt;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.sorter.CountrySorter;
import org.flexpay.ab.persistence.sorter.CountrySorterStub;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.List;

public class CountryDaoExtImpl extends JpaDaoSupport implements CountryDaoExt {

	/**
	 * Find and sort regions
	 *
	 * @param sorters Collection of sorters
	 * @param pager   Pager
	 * @return List of regions
	 */
	@SuppressWarnings ({"unchecked"})
	@NotNull
	@Override
	public List<Country> findCountries(Collection<? extends ObjectSorter> sorters, final Page<Country> pager) {
		CountrySorter sorter = findSorter(sorters);
		sorter.setCountryField("o");

		final StringBuilder cnthql = new StringBuilder();
		final StringBuilder hql = new StringBuilder();

		cnthql.append("select count(o) from Country o ");
		hql.append("select distinct o from Country o ");
		sorter.setFrom(hql);

		StringBuilder whereClause = new StringBuilder();
		whereClause.append(" where o.status=").append(Street.STATUS_ACTIVE);
		sorter.setWhere(whereClause);
		hql.append(whereClause);
		cnthql.append(" where o.status=").append(Street.STATUS_ACTIVE);

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
	private CountrySorter findSorter(Collection<? extends ObjectSorter> sorters) {

		for (ObjectSorter sorter : sorters) {
			if (sorter.isActivated() && sorter instanceof CountrySorter) {
				return (CountrySorter) sorter;
			}
		}

		return new CountrySorterStub();
	}

}
