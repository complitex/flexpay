package org.flexpay.ab.service;

import java.util.List;
import java.util.Locale;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.common.exception.FlexPayException;

/**
 * Service interface for TownTypes related tasks
 */
public interface TownTypeService extends
		MultilangEntityService<TownType, TownTypeTranslation> {

	/**
	 * Get a list of available town types
	 * 
	 * @return List of TownType
	 */
	List<TownType> getTownTypes();

	/**
	 * Initialize filter
	 * 
	 * @param townTypeFilter
	 *            filter to init
	 * @param locale
	 *            Locale to get names in
	 * @return initialized filter
	 * @throws FlexPayException
	 *             if failure occurs
	 */
	TownTypeFilter initFilter(TownTypeFilter townTypeFilter, Locale locale)
			throws FlexPayException;
}
