package org.flexpay.ab.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.CountryDao;
import org.flexpay.ab.dao.CountryDaoExt;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.CountryTranslation;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.flexpay.common.util.CollectionUtils.list;

@Transactional (readOnly = true)
public class CountryServiceImpl implements CountryService, ParentService<CountryFilter> {

	private Logger log = LoggerFactory.getLogger(getClass());

	private CountryDao countryDao;
	private CountryDaoExt countryDaoExt;

	/**
	 * Read Country object by its unique id
	 *
	 * @param countryStub Country stub
	 * @return Country object, or <code>null</code> if object not found
	 */
	@Nullable
	@Override
	public Country readFull(@NotNull Stub<Country> countryStub) {
		return countryDao.readFull(countryStub.getId());
	}

	/**
	 * Disable countries
	 *
	 * @param countryIds IDs of countries to disable
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(@NotNull Collection<Long> countryIds) {
		for (Long id : countryIds) {
			if (id == null) {
				log.warn("Null id in collection of country ids for disable");
				continue;
			}
			Country country = countryDao.read(id);
			if (country == null) {
				log.warn("Can't get country with id {} from DB", id);
				continue;
			}
			country.disable();
			countryDao.update(country);

			log.debug("Country disabled: {}", country);
		}
	}

	/**
	 * Read countries collection by theirs ids
	 *
 	 * @param countryIds Country ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found countries
	 */
	@NotNull
	@Override
	public List<Country> readFull(@NotNull Collection<Long> countryIds, boolean preserveOrder) {
		return countryDao.readFullCollection(countryIds, preserveOrder);
	}

	/**
	 * Create country
	 *
	 * @param country Country to save
	 * @return Saved instance of country
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Country create(@NotNull Country country) throws FlexPayExceptionContainer {

        log.debug("Creating country");

		validate(country);
		country.setId(null);
        log.debug("Creating country in DB");
		countryDao.create(country);
        log.debug("Country created {}", country);
		return country;

	}

	/**
	 * Validate country before save
	 *
	 * @param country Country object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull Country country) throws FlexPayExceptionContainer {

        log.debug("Validating country");

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		boolean defaultLangNameFound = false;
		boolean defaultLangShortNameFound = false;

		for (CountryTranslation translation : country.getTranslations()) {

			Language lang = translation.getLang();
			String name = translation.getName();
			String shortName = translation.getShortName();
			boolean nameNotEmpty = StringUtils.isNotEmpty(name);
			boolean shortNameNotEmpty = StringUtils.isNotEmpty(shortName);

			if (lang.isDefault()) {
				defaultLangNameFound = nameNotEmpty;
				defaultLangShortNameFound = shortNameNotEmpty;
			}

			if (nameNotEmpty) {
				List<Country> countries = countryDao.findByNameAndLanguage(name, lang.getId());
				if (!countries.isEmpty() && !countries.get(0).getId().equals(country.getId())) {
					container.addException(new FlexPayException(
							"Name \"" + name + "\" is already use", "ab.error.name_is_already_use", name));
				}
			}

			if (shortNameNotEmpty) {
				List<Country> countries = countryDao.findByShortNameAndLanguage(shortName, lang.getId());
				if (!countries.isEmpty() && !countries.get(0).getId().equals(country.getId())) {
					container.addException(new FlexPayException(
							"Short name \"" + shortName + "\" is already use", "ab.error.short_name_is_already_use", shortName));
				}
			}

		}

		if (!defaultLangNameFound) {
			container.addException(new FlexPayException(
					"No default language translation", "ab.error.country.full_name_is_required"));
		}
		if (!defaultLangShortNameFound) {
			container.addException(new FlexPayException(
					"No default language translation", "ab.error.country.short_name_is_required"));
		}

		if (container.isNotEmpty()) {
			throw container;
		}

        log.debug("Validating complete");
	}

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @param sorters Stack of sorters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@NotNull
	@Override
	public List<Country> find(@NotNull ArrayStack filters, @NotNull List<? extends ObjectSorter> sorters, @NotNull Page<Country> pager) {
		return countryDaoExt.findCountries(sorters, pager);
	}

	/**
	 * Lookup countries by query. Query is a string which may contains in country name:
	 *
	 * @param query searching string
	 * @return List of founded countries
	 */
	@NotNull
	@Override
	public List<Country> findByQuery(@NotNull String query) {
		return countryDao.findByQuery(query.toUpperCase());
	}

	/**
	 * Initialize filters
	 *
	 * @param filters Filters to init
	 * @param locale  Locale to get parent names in
	 * @return Initialised filters collection
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	@Override
	public ArrayStack initFilters(@Nullable ArrayStack filters, @NotNull Locale locale) throws FlexPayException {
		if (filters == null) {
			filters = new ArrayStack();
		}
		CountryFilter countryFilter = filters.isEmpty() ? null : (CountryFilter) filters.pop();
		countryFilter = initFilter(countryFilter, locale);
		filters.push(countryFilter);

		return filters;
	}

	/**
	 * Initialize parent filter. Possibly taking in account upper level forefather filter
	 *
	 * @param parentFilter	Filter to init
	 * @param foreFatherFilter	Upper level filter
	 * @param locale	Locale to get parent names in
	 * @return Initialised filter
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	@Override
	public CountryFilter initFilter(@Nullable CountryFilter parentFilter, @Nullable PrimaryKeyFilter<?> foreFatherFilter, @NotNull Locale locale) throws FlexPayException {
		return initFilter(parentFilter, locale);
	}

	/**
	 * Initialise filter with the list of available countries
	 *
	 * @param countryFilter Filter to init
	 * @param locale		Locale to get countries names in
	 * @return Updated filter
	 * @throws FlexPayException if languages configuration is invalid
	 */
	@NotNull
	private CountryFilter initFilter(@Nullable CountryFilter countryFilter, @NotNull Locale locale) throws FlexPayException {

		List<CountryTranslation> translations = getTranslations(locale);
		if (translations.isEmpty()) {
			throw new FlexPayException("No country names", "ab.error.country.no_countries");
		}

		if (countryFilter == null) {
			countryFilter = new CountryFilter();
		}

		countryFilter.setNames(translations);

		if (countryFilter.getSelectedId() == null) {
			countryFilter.setSelectedId(translations.get(0).getId());
		}

		return countryFilter;
	}

	/**
	 * Get countries translations for specified locale,
	 * if translation for specified locale is not found check for
	 * translation in default locale
	 *
	 * @param locale Locale to get translations for
	 * @return List of name translations for all countries
	 */
	@NotNull
	private List<CountryTranslation> getTranslations(@NotNull Locale locale) {

		List<Country> countries = countryDao.listCountries(Country.STATUS_ACTIVE);
		List<CountryTranslation> translations = list();

		for (Country country : countries) {
			CountryTranslation translation = TranslationUtil.getTranslation(country.getTranslations(), locale);
			if (translation == null) {
				log.warn("No name for country: {}", country);
				continue;
			}
			translations.add(translation);
		}

		return translations;
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        countryDao.setJpaTemplate(jpaTemplate);
        countryDaoExt.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setCountryDao(CountryDao countryDao) {
		this.countryDao = countryDao;
	}

	@Required
	public void setCountryDaoExt(CountryDaoExt countryDaoExt) {
		this.countryDaoExt = countryDaoExt;
	}

}
