package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BuildingsDao extends GenericDao<BuildingAddress, Long> {

	/**
	 * Find buildings in the street
	 *
	 * @param streetId Street identifier
	 * @return list of buildings for the street
	 */
	List<BuildingAddress> findBuildings(Long streetId);

	/**
	 * Find buildings in the street
	 *
	 * @param streetId Street identifier
	 * @param pager Page instance
	 * @return list of buildings for the street
	 */
	List<BuildingAddress> findBuildings(Long streetId, Page<BuildingAddress> pager);

	/**
	 * Find buildings in the street and district
	 *
	 * @param streetId Street identifier
	 * @param districtId District identifier
	 * @param pager Page instance
	 * @return list of buildings for the street
	 */
	List<BuildingAddress> findStreetDistrictBuildings(Long streetId, Long districtId, Page<BuildingAddress> pager);

	/**
	 * Find buildings relations for building
	 *
	 * @param buildingId Building identifier
	 * @return list of buildings for the building
	 */
	List<BuildingAddress> findBuildingBuildings(@NotNull Long buildingId);
}
