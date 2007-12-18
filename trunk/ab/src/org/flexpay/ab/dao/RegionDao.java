package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Region;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;

import java.util.List;

public interface RegionDao extends GenericDao<Region, Long> {

	/**
	 * Get all regions for country
	 *
	 * @param page	  Regions list pager
	 * @param status	Regions status to retrive
	 * @param countryId Country id
	 * @return List of country regions
	 */
	List<Region> listRegions(Page page, int status, Long countryId);

	/**
	 * Get all regions for country
	 *
	 * @param status	Regions status to retrive
	 * @param countryId Country id
	 * @return List of country regions
	 */
	List<Region> listRegions(int status, Long countryId);
}
