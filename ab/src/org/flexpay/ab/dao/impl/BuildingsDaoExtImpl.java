package org.flexpay.ab.dao.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.dao.BuildingsDaoExt;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.filters.DistrictFilter;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.persistence.sorter.BuildingsSorter;
import org.flexpay.ab.persistence.sorter.BuildingsSorterStub;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collection;
import java.util.List;

public class BuildingsDaoExtImpl extends HibernateDaoSupport implements BuildingsDaoExt {

    private final Logger log = LoggerFactory.getLogger(getClass());

	private AddressAttributeTypeService addressAttributeTypeService;

	/**
	 * Find building by number
	 *
	 * @param streetId   Building street key
	 * @param districtId Building district key
	 * @param number	 Building number
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	@SuppressWarnings ({"unchecked"})
	@NotNull
	@Override
	public List<BuildingAddress> findBuildings(@NotNull Long streetId, @NotNull Long districtId, @NotNull String number) {
		Object[] params = {districtId, streetId,
						   ApplicationConfig.getBuildingAttributeTypeNumber().getId(), number};
		return getHibernateTemplate().findByNamedQuery("BuildingAddress.findByNumberWithDistrict", params);
	}

	/**
	 * Find building by number
	 *
	 * @param streetId Street key
	 * @param number   Building number
	 * @return Buildingses list
	 */
	@SuppressWarnings ({"unchecked"})
	@NotNull
	@Override
	public List<BuildingAddress> findBuildings(@NotNull Long streetId, @NotNull String number) {
		Object[] params = {streetId,
						   ApplicationConfig.getBuildingAttributeTypeNumber().getId(), number};
		return getHibernateTemplate().findByNamedQuery("BuildingAddress.findByNumber", params);
	}

	/**
	 * Find Building stub by Buildings id
	 *
	 * @param buildingsId Buildings key
	 * @return Building if found, or <code>null</code> otherwise
	 */
	@SuppressWarnings ({"unchecked"})
	@Nullable
	@Override
	public Building findBuilding(@NotNull Long buildingsId) {
		List<Building> buildings = getHibernateTemplate().findByNamedQuery("BuildingAddress.findBuilding", buildingsId);
		if (buildings.isEmpty()) {
			return null;
		}

		return buildings.get(0);
	}

	/**
	 * Find and sort buildings
	 *
	 * @param filters Building filters
	 * @param sorters Collection of sorters
	 * @param pager   Pager
	 * @return List of addresses
	 */
	@SuppressWarnings ({"unchecked"})
	@NotNull
	@Override
	public List<BuildingAddress> findBuildingAddresses(
			ArrayStack filters, Collection<? extends ObjectSorter> sorters, final Page<BuildingAddress> pager) {
		BuildingsSorter sorter = findSorter(sorters);
		sorter.setBuildingsField("bs");
		sorter.setTypes(addressAttributeTypeService.getAttributeTypes());

		final StringBuilder cntHql = new StringBuilder();
		final StringBuilder hql = new StringBuilder();

		cntHql.append("select count(bs) from BuildingAddress bs left outer join bs.building b ");
		hql.append("select distinct bs from BuildingAddress bs left outer join bs.building b ");
		sorter.setFrom(hql);

		StringBuilder whereClause = new StringBuilder();
		whereClause.append(" where bs.status=").append(Apartment.STATUS_ACTIVE);
		for (Object obj : filters) {
			ObjectFilter objectFilter = (ObjectFilter) obj;
			if (!objectFilter.needFilter()) {
				continue;
			}
			if (obj instanceof StreetFilter) {
				StreetFilter filter = (StreetFilter) obj;
				whereClause.append(" and bs.street.id=").append(filter.getSelectedId());
			}
			if (obj instanceof DistrictFilter) {
				DistrictFilter filter = (DistrictFilter) obj;
				whereClause.append(" and b.district.id=").append(filter.getSelectedId());
			}
		}
		cntHql.append(whereClause.toString());
		sorter.setWhere(whereClause);
		hql.append(whereClause);

		StringBuilder orderByClause = new StringBuilder();
		sorter.setOrderBy(orderByClause);
		if (orderByClause.length() > 0) {
			hql.append(" ORDER BY ").append(orderByClause);
		}

		return getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public List<?> doInHibernate(Session session) throws HibernateException {
				Query cntQuery = session.createQuery(cntHql.toString());
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
	private BuildingsSorter findSorter(Collection<? extends ObjectSorter> sorters) {

		for (ObjectSorter sorter : sorters) {
			if (sorter.isActivated() && sorter instanceof BuildingsSorter) {
				return (BuildingsSorter) sorter;
			}
		}

		return new BuildingsSorterStub();
	}

    @Required
	public void setAddressAttributeTypeService(AddressAttributeTypeService addressAttributeTypeService) {
		this.addressAttributeTypeService = addressAttributeTypeService;
	}

}
