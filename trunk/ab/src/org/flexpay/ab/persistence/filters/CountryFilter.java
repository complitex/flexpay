package org.flexpay.ab.persistence.filters;

import org.flexpay.ab.persistence.CountryNameTranslation;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.filter.NameFilter;

/**
 * Country filter class
 */
public class CountryFilter extends NameFilter<Country, CountryNameTranslation> {

	public CountryFilter() {
		setDefaultId(ApplicationConfig.getDefaultCountry().getId());
	}
}