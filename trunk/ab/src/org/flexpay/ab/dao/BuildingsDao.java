package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Buildings;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;

import java.util.List;

public interface BuildingsDao extends GenericDao<Buildings, Long> {

	/**
	 * Find buildings in the street
	 *
	 * @param streetId Street identifier
	 * @param pager Page instance
	 * @return list of buildings for the street
	 */
	List<Buildings> findBuildings(Long streetId, Page pager);

	/**
	 * Find buildings relations for building
	 *
	 * @param buildingId Building identifier
	 * @param page Page instance
	 * @return list of buildings for the building
	 */
	List<Buildings> findBuildingBuildings(Long buildingId, Page page);
}
