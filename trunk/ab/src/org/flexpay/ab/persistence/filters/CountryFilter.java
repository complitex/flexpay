package org.flexpay.ab.persistence.filters;

import org.flexpay.ab.persistence.CountryNameTranslation;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.filter.NameFilter;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

/**
 * Country filter class
 */
public class CountryFilter extends NameFilter<Country, CountryNameTranslation> {

	public CountryFilter() {
		setDefaultId(ApplicationConfig.getDefaultCountry().getId());
	}

	public CountryFilter(Long selectedId) {
		super(selectedId);
	}

	public CountryFilter(@NotNull Stub<Country> countryStub) {
		super(countryStub);
	}
}
