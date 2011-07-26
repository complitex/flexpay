package org.flexpay.common.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;

import java.util.Locale;

/**
 * Service for parents of objects with time-dependent names
 */
public interface ParentService<PKF extends PrimaryKeyFilter<?>> extends JpaSetService {

	/**
	 * Initialize parent filter. Possibly taking in account upper level forefather filter
	 *
	 * @param parentFilter	 Filter to init
	 * @param forefatherFilter Upper level filter
	 * @param locale		   Locale to get parent names in
	 * @return Initialised filter
	 * @throws FlexPayException if failure occurs
	 */
	PKF initFilter(PKF parentFilter, PrimaryKeyFilter<?> forefatherFilter, Locale locale)
			throws FlexPayException;

	/**
	 * Initialize filters. <p>Filters are coming from the most significant to less
	 * significant ones order, like CountryFilter, RegionFilter, TownFilter for example</p>
	 *
	 * @param filters Filters to init
	 * @param locale  Locale to get parent names in
	 * @return Initialised filters stack
	 * @throws FlexPayException if failure occurs
	 */
	ArrayStack initFilters(ArrayStack filters, Locale locale) throws FlexPayException;

}
