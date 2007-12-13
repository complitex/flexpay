package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DateInterval;

import java.util.List;
import java.util.Locale;
import java.util.Collection;

public interface RegionService {

	/**
	 * Create new Region
	 *
	 * @param regionNames   Region names with translations
	 * @param countryFilter Country filter
	 * @return persisted Region object
	 * @throws FlexPayExceptionContainer if operation fails
	 */
	Region create(List<RegionName> regionNames, CountryFilter countryFilter)
			throws FlexPayExceptionContainer;

	/**
	 * Read Region object by its unique id
	 *
	 * @param id Region key
	 * @return Region object, or <code>null</code> if object not found
	 */
	public Region read(Long id);

	/**
	 * Get Region name translations for specified locale, if translation is not found check
	 * for translation in default locale
	 *
	 * @param locale		Locale to get translations for
	 * @param countryFilter Country filter
	 * @param dateInterval  Date interval
	 * @return List of region names
	 * @throws FlexPayException if failure occurs
	 */
	List<RegionName> getRegionNames(Locale locale, CountryFilter countryFilter, DateInterval dateInterval)
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
	 * Update region names
	 * @param region Region to update
	 * @param regionNames Updated region names
	 * @param countryFilter Country Filter
	 * @throws FlexPayExceptionContainer if failure occurs
	 */
	void update(Region region, Collection<RegionName> regionNames, CountryFilter countryFilter) throws FlexPayExceptionContainer;
}
