package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.CountryNameTranslation;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.springframework.security.annotation.Secured;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.collections.ArrayStack;

import java.util.List;
import java.util.Locale;

public interface CountryService extends ParentService<CountryFilter> {

	/**
	 * Initialize parent filter. Possibly taking in account upper level forefather filter
	 *
	 * @param parentFilter	 Filter to init
	 * @param forefatherFilter Upper level filter
	 * @param locale		   Locale to get parent names in
	 * @return Initialised filter
	 * @throws FlexPayException if failure occurs
	 */
	@Secured (Roles.COUNTRY_READ)
	CountryFilter initFilter(CountryFilter parentFilter, PrimaryKeyFilter forefatherFilter, Locale locale)
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
	@Secured (Roles.COUNTRY_READ)
	ArrayStack initFilters(ArrayStack filters, Locale locale) throws FlexPayException;

	/**
	 * Create country
	 *
	 * @param countryNames Country name translations
	 * @return persisted Country object
	 */
	@Secured (Roles.COUNTRY_ADD)
	Country create(List<CountryNameTranslation> countryNames);

	/**
	 * Get countries translations for specified locale, if translation for specified locale is not found check for
	 * translation in default locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of countries
	 * @throws FlexPayException if failure occurs
	 */
	@Secured (Roles.COUNTRY_READ)
	List<CountryNameTranslation> getCountries(@NotNull Locale locale) throws FlexPayException;

	@Secured (Roles.COUNTRY_READ)
	Country readFull(@NotNull Stub<Country> stub);

    /**
     * Checks whether there is no country with name <code>countryName</code> in language <code>language</code>
     * @param countryName country full name
     * @param language search language
     * @return <code>true</code> if there is no country with the name in the language, <code>false</code> otherwise
     */
    @Secured (Roles.COUNTRY_READ)
    boolean isNameAvailable(@NotNull String countryName, @NotNull Language language);

    /**
     * Checks whether there is no country with short name <code>shortName</code> in language <code>language</code>
     * @param shortName country short name
     * @param language search language
     * @return <code>true</code> if there is no country with the short name in the language, <code>false</code> otherwise
     */
    @Secured (Roles.COUNTRY_READ)
    boolean isShortNameAvailable(@NotNull String shortName, @NotNull Language language);
}
