package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.RegionNameTranslation;
import org.flexpay.ab.persistence.RegionNameTemporal;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RegionService {

	/**
	 * Create new Region with a single name
	 *
	 * @param nameTranslations Region name translations
	 * @param countryFilter	Country filter
	 * @param from			 Date from which the name is valid
	 * @return persisted Region object
	 * @throws FlexPayExceptionContainer if operation fails
	 */
	Region create(List<RegionNameTranslation> nameTranslations,
				  CountryFilter countryFilter, Date from)
			throws FlexPayExceptionContainer;

	/**
	 * Read Region object by its unique id
	 *
	 * @param id Region key
	 * @return Region object, or <code>null</code> if object not found
	 */
	public Region read(Long id);

	/**
	 * Read Region name temporal object by its unique id
	 *
	 * @param id Region key
	 * @return Region name temporal object, or <code>null</code> if object not found
	 */
	public RegionNameTemporal readRegionNameTemporal(Long id);

	/**
	 * Get Region names
	 *
	 * @param countryFilter Country filter
	 * @param pager		 Regions list pager
	 * @return List of region names
	 * @throws FlexPayException if failure occurs
	 */
	List<RegionName> getRegionNames(CountryFilter countryFilter, Page pager)
			throws FlexPayException;

	/**
	 * Get a list of available country regions
	 *
	 * @param countryFilter CountryFilter
	 * @return List of Regions
	 */
	List<Region> getRegions(CountryFilter countryFilter);

	/**
	 * Disable Regions
	 *
	 * @param regions Regions to disable
	 * @throws FlexPayExceptionContainer if failure occurs
	 */
	void disable(Collection<Region> regions) throws FlexPayExceptionContainer;

	/**
	 * Get region name translations for temporal
	 *
	 * @param temporalId Temporal id
	 * @return Mapping from language ids to translations
	 */
	Map<Long, RegionNameTranslation> getTranslations(Long temporalId);

	/**
	 * Save region name translations
	 *
	 * @param region		   Region to update
	 * @param temporalId	   Temporal id to apply changes for
	 * @param nameTranslations New translations
	 * @param date			 Date from which the name is valid
	 * @return updated region instance
	 * @throws FlexPayExceptionContainer exceptions container
	 */
	Region save(Region region, Long temporalId, List<RegionNameTranslation> nameTranslations, Date date)
			throws FlexPayExceptionContainer;
}