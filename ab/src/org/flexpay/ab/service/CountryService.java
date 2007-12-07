package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.CountryNameTranslation;
import org.flexpay.common.exception.FlexPayException;

import java.util.List;
import java.util.Locale;

public interface CountryService {

	/**
	 * Create country
	 *
	 * @param countryNames Country name translations
	 * @return created Country object
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
}
