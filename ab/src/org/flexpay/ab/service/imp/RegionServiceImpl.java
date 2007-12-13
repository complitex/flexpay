package org.flexpay.ab.service.imp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.CountryDao;
import org.flexpay.ab.dao.RegionDao;
import org.flexpay.ab.dao.RegionNameDao;
import org.flexpay.ab.dao.RegionNameTranslationDao;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.RegionName;
import org.flexpay.ab.persistence.RegionNameTranslation;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DateInterval;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.TranslationUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Region service layer implementation
 */
@Transactional (readOnly = true, rollbackFor = Exception.class)
public class RegionServiceImpl implements RegionService {

	private static Logger log = Logger.getLogger(RegionServiceImpl.class);

	private RegionDao regionDao;
	private RegionNameDao regionNameDao;
	private RegionNameTranslationDao regionNameTranslationDao;
	private CountryDao countryDao;

	/**
	 * {@inheritDoc}
	 */
	@Transactional (readOnly = false)
	public Region create(List<RegionName> regionNames, CountryFilter countryFilter)
			throws FlexPayExceptionContainer {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		Country country = getCountry(countryFilter, container);

		// todo check if intervals are intersecting
		Set<RegionName> names = new HashSet<RegionName>();
		for (RegionName regionName : regionNames) {
			RegionName name = new RegionName();
			boolean hasDefaultTranslation = false;
			Set<RegionNameTranslation> translations = new HashSet<RegionNameTranslation>();
			for (RegionNameTranslation translation : regionName.getTranslations()) {
				if (StringUtils.isNotBlank(translation.getName())) {
					translations.add(translation);
					hasDefaultTranslation |= translation.getLang().isDefault();
				}
			}

			if (!hasDefaultTranslation) {
				FlexPayException e = new FlexPayException("No default language region translation",
						"error.region_no_default_translation",
						DateIntervalUtil.format(regionName));
				container.addException(e);
				continue;
			}

			name.setTranslations(translations);
			name.setBegin(regionName.getBegin());
			name.setEnd(regionName.getEnd());
			names.add(name);
		}

		if (!container.getExceptions().isEmpty()) {
			throw container;
		}

		Region region = new Region();
		region.setStatus(Region.STATUS_ACTIVE);
		region.setNames(names);
		region.setCountry(country);
		regionDao.create(region);

		for (RegionName regionName : names) {
			regionName.setRegion(region);
			regionNameDao.create(regionName);
			for (RegionNameTranslation translation : regionName.getTranslations()) {
				translation.setRegionName(regionName);
				regionNameTranslationDao.create(translation);
			}
		}

		return region;
	}

	/**
	 * Get Region name translations for specified locale, if translation is not found check
	 * for translation in default locale
	 *
	 * @param locale		Locale to get translations for
	 * @param countryFilter Country filter
	 * @param dateInterval  DateInterval
	 * @return List of region names
	 * @throws FlexPayException if failure occurs
	 */
	public List<RegionName> getRegionNames(Locale locale, CountryFilter countryFilter, DateInterval dateInterval)
			throws FlexPayException {

		if (log.isDebugEnabled()) {
			log.debug("Getting list of Region names: " + countryFilter);
		}

		List<Region> regions = regionDao.listRegions(
				Region.STATUS_ACTIVE, countryFilter.getSelectedCountryId());
		List<RegionName> regionNames = new ArrayList<RegionName>(regions.size());

		if (log.isDebugEnabled()) {
			log.debug("RegionNames: " + regions);
		}

		for (Region region : regions) {
			RegionName regionName = (RegionName) DateIntervalUtil.getInterval(
					region.getNames(), dateInterval);
			if (regionName == null) {
				log.error("No name for region in interval: " + dateInterval);
				continue;
			}

			RegionNameTranslation translation = (RegionNameTranslation) TranslationUtil
					.getTranslation(regionName.getTranslations(), locale);
			regionName.setTranslation(translation);

			if (log.isDebugEnabled()) {
				log.debug("Found translation: " + translation);
			}

			if (translation == null) {
				log.error("No translation for region name: " + regionName);
				continue;
			}
			translation.setLangTranslation(
					LanguageUtil.getLanguageName(translation.getLang(), locale));

			regionNames.add(regionName);
		}

		return regionNames;
	}

	/**
	 * Get a list of available country regions
	 *
	 * @param countryFilter CountryFilter
	 * @return List of Regions
	 */
	public List<Region> getRegions(CountryFilter countryFilter) {
		return regionDao.listRegions(
				Region.STATUS_ACTIVE, countryFilter.getSelectedCountryId());
	}

	/**
	 * Disable Regions TODO: check if there are any towns in specified regions and reject
	 *
	 * @param regions Regions to disable
	 * @throws FlexPayExceptionContainer if failure occurs
	 */
	@Transactional (readOnly = false)
	public void disable(Collection<Region> regions) throws FlexPayExceptionContainer {
		if (log.isDebugEnabled()) {
			log.debug(regions.size() + " regions to disable");
		}

		for (Region region : regions) {
			region.setStatus(Region.STATUS_DISABLED);
			regionDao.update(region);

			if (log.isDebugEnabled()) {
				log.debug("Disabled: " + region);
			}
		}
	}

	/**
	 * Update region names
	 *
	 * @param region		Region to update
	 * @param regionNames   Updated region names
	 * @param countryFilter CountryFilter
	 * @throws FlexPayExceptionContainer if failure occurs
	 */
	@Transactional (readOnly = false)
	public void update(Region region, Collection<RegionName> regionNames, CountryFilter countryFilter)
			throws FlexPayExceptionContainer {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();
		Country country = getCountry(countryFilter, container);

		// todo check if intervals are intersecting
		Set<RegionName> names = new HashSet<RegionName>();
		for (RegionName regionName : regionNames) {
			RegionName name = new RegionName();
			boolean hasDefaultTranslation = false;
			Set<RegionNameTranslation> translations = new HashSet<RegionNameTranslation>();
			for (RegionNameTranslation translation : regionName.getTranslations()) {
				if (StringUtils.isNotBlank(translation.getName())) {
					translations.add(translation);
					hasDefaultTranslation |= translation.getLang().isDefault();
				}
			}

			if (!hasDefaultTranslation) {
				FlexPayException e = new FlexPayException("No default language region translation",
						"error.region_no_default_translation",
						DateIntervalUtil.format(regionName));
				container.addException(e);
				continue;
			}

			name.setId(regionName.getId());
			name.setTranslations(translations);
			name.setBegin(regionName.getBegin());
			name.setEnd(regionName.getEnd());
			names.add(name);
		}

		if (!container.getExceptions().isEmpty()) {
			throw container;
		}

		Collection<RegionName> namesToDelete = getRegionNamesToDelete(region, names);

		region.setNames(names);
		region.setCountry(country);
		regionDao.update(region);

		for (RegionName regionName : names) {
			regionName.setRegion(region);
			if (regionName.getId() == null) {
				regionNameDao.create(regionName);
			} else {
				regionNameDao.update(regionName);
			}
			for (RegionNameTranslation translation : regionName.getTranslations()) {
				translation.setRegionName(regionName);
				if (translation.getId() != null) {
					regionNameTranslationDao.update(translation);
				} else {
					regionNameTranslationDao.create(translation);
				}
			}
		}

		for (RegionName name : namesToDelete) {
			for (RegionNameTranslation nameTranslation : name.getTranslations()) {
				regionNameTranslationDao.delete(nameTranslation);
			}
			regionNameDao.delete(name);
		}
	}

	private Collection<RegionName> getRegionNamesToDelete(
			Region region, Collection<RegionName> namesToUpdate) {

		Collection<RegionName> names = region.getNames();
		Collection<RegionName> namesToDelete = new ArrayList<RegionName>();

		OUTER:
		for (RegionName name : names) {
			for (RegionName updateName : namesToUpdate) {
				if (name.getId().equals(updateName.getId())) {
					break OUTER;
				}
			}

			namesToDelete.add(name);
		}

		return namesToDelete;
	}

	private Country getCountry(CountryFilter countryFilter, FlexPayExceptionContainer container) throws FlexPayExceptionContainer {
		if (countryFilter.getSelectedCountryId() == null) {
			container.addException(new FlexPayException("null", "ab.region.no_country_selectected"));
			throw container;
		}

		Country country = countryDao.read(countryFilter.getSelectedCountryId());
		if (country == null) {
			container.addException(new FlexPayException("null", "ab.region.country_id_invalid"));
			throw container;
		}
		return country;
	}

	/**
	 * Read Region object by its unique id
	 *
	 * @param id Region key
	 * @return Region object, or <code>null</code> if object not found
	 */
	public Region read(Long id) {
		return regionDao.read(id);
	}

	/**
	 * Setter for property 'regionDao'.
	 *
	 * @param regionDao Value to set for property 'regionDao'.
	 */
	public void setRegionDao(RegionDao regionDao) {
		this.regionDao = regionDao;
	}

	/**
	 * Setter for property 'regionNameDao'.
	 *
	 * @param regionNameDao Value to set for property 'regionNameDao'.
	 */
	public void setRegionNameDao(RegionNameDao regionNameDao) {
		this.regionNameDao = regionNameDao;
	}

	/**
	 * Setter for property 'countryDao'.
	 *
	 * @param countryDao Value to set for property 'countryDao'.
	 */
	public void setCountryDao(CountryDao countryDao) {
		this.countryDao = countryDao;
	}

	/**
	 * Setter for property 'regionNameTranslationDao'.
	 *
	 * @param regionNameTranslationDao Value to set for property 'regionNameTranslationDao'.
	 */
	public void setRegionNameTranslationDao(RegionNameTranslationDao regionNameTranslationDao) {
		this.regionNameTranslationDao = regionNameTranslationDao;
	}
}
