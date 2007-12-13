package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.CountryNameTranslation;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.common.exception.FlexPayException;

import java.util.List;
import java.util.Locale;

public interface CountryService {

	/**
	 * Create country
	 *
	 * @param countryNames Country name translations
	 * @return persisted Country object
	 */
	Country create(List<CountryNameTranslation> countryNames);

	/**
	 * Get countries translations for specified locale, if translation for specified locale
	 * is not found check for translation in default locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of countries
	 * @throws FlexPayException if failure occurs
	 */
	List<CountryNameTranslation> getCountries(Locale locale) throws FlexPayException;

	/**
	 * Initialise filter with the list of available countries
	 *
	 * @param countryFilter Filter to init
	 * @param locale Locale to get countries names in
	 * @return Updated filter
	 * @throws FlexPayException iflanguage configuration is invalid
	 */
	CountryFilter initFilter(CountryFilter countryFilter, Locale locale)
			throws FlexPayException;
}
