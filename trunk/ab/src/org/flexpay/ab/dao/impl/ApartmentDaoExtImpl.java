package org.flexpay.ab.dao.impl;

import org.flexpay.ab.dao.ApartmentDaoExt;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.sorter.ApartmentSorter;
import org.flexpay.ab.persistence.sorter.ApartmentSorterStub;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.List;

public class ApartmentDaoExtImpl extends SimpleJdbcDaoSupport implements ApartmentDaoExt {

	private JpaTemplate jpaTemplate;

	/**
	 * Find apartment by number
	 *
	 * @param building Building to find apartment in
	 * @param number   Building number
	 * @return Apartment instance, or <code>null</null> if not found
	 */
	@Nullable
	@Override
	public Stub<Apartment> findApartmentStub(@NotNull Building building, final String number) {
		String sql = "SELECT id FROM ab_apartments_tbl a WHERE a.building_id=? AND EXISTS " +
					 "(SELECT 1 FROM ab_apartment_numbers_tbl n WHERE n.apartment_id=a.id AND n.value=? AND n.end_date>?)";

		Object[] args = {building.getId(), number, DateUtil.now()};
		List<?> result = getJdbcTemplate().query(sql, args, new SingleColumnRowMapper<Long>(Long.class));

		return result.isEmpty() ? null : new Stub<Apartment>((Long) result.get(0));
	}

	/**
	 * Find and sort apartments
	 *
	 * @param buildingId Building key
	 * @param sorters	Collection of sorters
	 * @param pager	  Pager
	 * @return List of apartments
	 */
	@SuppressWarnings ({"unchecked"})
	@NotNull
	@Override
	public List<Apartment> findApartments(Long buildingId, Collection<? extends ObjectSorter> sorters, final Page<Apartment> pager) {
		ApartmentSorter sorter = findSorter(sorters);
		sorter.setApartmentField("a");

		final StringBuilder cntHql = new StringBuilder();
		final StringBuilder hql = new StringBuilder();

		cntHql.append("select count(a) from Apartment a ");
		hql.append("select distinct a from Apartment a ");
		sorter.setFrom(hql);

		StringBuilder whereClause = new StringBuilder();
		whereClause.append(" where a.building.id=").append(buildingId).append(" and a.status=").append(Apartment.STATUS_ACTIVE);
		sorter.setWhere(whereClause);
		hql.append(whereClause);
		cntHql.append(" where a.building.id=").append(buildingId).append(" and a.status=").append(Apartment.STATUS_ACTIVE);

		StringBuilder orderByClause = new StringBuilder();
		sorter.setOrderBy(orderByClause);
		if (orderByClause.length() > 0) {
			hql.append(" ORDER BY ").append(orderByClause);
		}

		return jpaTemplate.executeFind(new JpaCallback() {
			@Override
			public Object doInJpa(EntityManager entityManager) throws PersistenceException {
				javax.persistence.Query cntQuery = entityManager.createQuery(cntHql.toString());
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
	private ApartmentSorter findSorter(Collection<? extends ObjectSorter> sorters) {

		for (ObjectSorter sorter : sorters) {
			if (sorter.isActivated() && sorter instanceof ApartmentSorter) {
				return (ApartmentSorter) sorter;
			}
		}

		return new ApartmentSorterStub();
	}

    @Override
	@Required
	public void setJpaTemplate(JpaTemplate jpaTemplate) {
		this.jpaTemplate = jpaTemplate;
	}

}
