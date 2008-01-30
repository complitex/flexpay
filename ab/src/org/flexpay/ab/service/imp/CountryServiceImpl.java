package org.flexpay.ab.service.imp;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.CountryDao;
import org.flexpay.ab.dao.CountryNameDao;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.CountryNameTranslation;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class CountryServiceImpl implements CountryService {

	private static Logger log = Logger.getLogger(CountryServiceImpl.class);

	private CountryDao countryDao;
	private CountryNameDao countryNameDao;

	@Transactional (readOnly = false)
	public Country create(List<CountryNameTranslation> countryNames) {
		Country country = new Country();
		country.setStatus(Country.STATUS_ACTIVE);

		if (log.isInfoEnabled()) {
			log.info("Country names to persiste: " + countryNames);
		}

		Set<CountryNameTranslation> names = new HashSet<CountryNameTranslation>();
		for (CountryNameTranslation name : countryNames) {
			if (!StringUtils.isBlank(name.getName())) {
				name.setTranslatable(country);
				names.add(name);
			}
		}
		if (names.size() == 0) {
			throw new RuntimeException("No country names specified");
		}

		// Save country
		countryDao.create(country);
		for (CountryNameTranslation name : names) {
			countryNameDao.create(name);
		}
		country.setCountryNames(names);

		if (log.isInfoEnabled()) {
			log.info("Country: " + country);
		}

		return country;
	}

	public List<CountryNameTranslation> getCountries(Locale locale) throws FlexPayException {
		Language language = LanguageUtil.getLanguage(locale);
		Language defaultLang = ApplicationConfig.getInstance().getDefaultLanguage();
		List<Country> countries = countryDao.listCountries();

		log.info("Found " + countries.size() + " countries");

		List<CountryNameTranslation> countryNameList = new ArrayList<CountryNameTranslation>();

		for (Country country : countries) {
			CountryNameTranslation name = getCountryName(country, language, defaultLang);
			if (name == null) {
				log.error("No name for country: " + language.getLangIsoCode() + " : " +
						  defaultLang.getLangIsoCode() + ", " + country);
				continue;
			}
			name.setLangTranslation(LanguageUtil.getLanguageName(name.getLang(), locale));
			countryNameList.add(name);
		}

		return countryNameList;
	}

	private CountryNameTranslation getCountryName(Country country, Language lang, Language defaultLang) {
		CountryNameTranslation defaultName = null;

		for (CountryNameTranslation name : country.getCountryNames()) {
			if (lang.equals(name.getLang())) {
				return name;
			}
			if (defaultLang.equals(name.getLang())) {
				defaultName = name;
			}
		}

		return defaultName;
	}

	/**
	 * Initialise filter with the list of available countries
	 *
	 * @param countryFilter Filter to init
	 * @param locale		Locale to get countries names in
	 * @return Updated filter
	 * @throws FlexPayException if languages configuration is invalid
	 */
	public CountryFilter initFilter(CountryFilter countryFilter, Locale locale)
			throws FlexPayException {

		if (countryFilter == null) {
			countryFilter = new CountryFilter();
		}

		countryFilter.setNames(getCountries(locale));
		Collection<CountryNameTranslation> names = countryFilter.getNames();
		if (names.isEmpty()) {
			throw new FlexPayException("No country names", "ab.no_countries");
		}

		if (countryFilter.getSelectedId() == null) {
			Country firstCountry = (Country) names.iterator().next().getTranslatable();
			countryFilter.setSelectedId(firstCountry.getId());
		}

		return countryFilter;
	}

	/**
	 * Initialize filters
	 *
	 * @param filters Filters to init
	 * @param locale  Locale to get parent names in
	 * @return Initialised filters collection
	 * @throws FlexPayException if failure occurs
	 */
	public ArrayStack initFilters(ArrayStack filters, Locale locale) throws FlexPayException {
		if (filters == null) {
			filters = new ArrayStack();
		}
		CountryFilter countryFilter = filters.isEmpty() ?
									  null : (CountryFilter)  filters.pop();
		countryFilter = initFilter(countryFilter, locale);
		filters.push(countryFilter);

		return filters;
	}

	/**
	 * Initialize parent filter. Possibly taking in account upper level forefather filter
	 *
	 * @param parentFilter	 Filter to init
	 * @param foreFatherFilter Upper level filter
	 * @param locale		   Locale to get parent names in
	 * @return Initialised filter
	 * @throws FlexPayException if failure occurs
	 */
	public CountryFilter initFilter(CountryFilter parentFilter, PrimaryKeyFilter foreFatherFilter, Locale locale)
			throws FlexPayException {
		return initFilter(parentFilter, locale);
	}

	public void setCountryDao(CountryDao countryDao) {
		this.countryDao = countryDao;
	}

	public void setCountryNameDao(CountryNameDao countryNameDao) {
		this.countryNameDao = countryNameDao;
	}
}
