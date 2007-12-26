package org.flexpay.ab.service.imp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.*;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.util.DateIntervalUtil;
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
	private RegionNameTemporalDao regionNameTemporalDao;
	private RegionNameTranslationDao regionNameTranslationDao;
	private CountryDao countryDao;

	/**
	 * {@inheritDoc}
	 */
	@Transactional (readOnly = false)
	public Region create(List<RegionNameTranslation> nameTranslations,
						 CountryFilter countryFilter, Date date)
			throws FlexPayExceptionContainer {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();
		Country country = getCountry(countryFilter, container);
		Set<RegionNameTranslation> names = getTranslations(nameTranslations, container);

		Region region = new Region();
		region.setStatus(Region.STATUS_ACTIVE);

		RegionNameTemporal nameTemporal = new RegionNameTemporal();
		nameTemporal.setRegion(region);
		nameTemporal.setBegin(date);
		TimeLine<RegionName, RegionNameTemporal> tl =
				new TimeLine<RegionName, RegionNameTemporal>(nameTemporal);
		region.setNamesTimeLine(tl);
		region.setCountry(country);
		regionDao.create(region);

		RegionName regionName = new RegionName();
		regionName.setTranslations(names);
		regionName.setRegion(region);
		regionNameDao.create(regionName);

		for (RegionNameTranslation translation : regionName.getTranslations()) {
			translation.setRegionName(regionName);
			regionNameTranslationDao.create(translation);
		}

		nameTemporal.setValue(regionName);
		for (RegionNameTemporal temporal : region.getNamesTimeLine().getIntervals()) {
			RegionName empty = new RegionName();
			if (nameTemporal.getValue().equals(empty)) {
				nameTemporal.setValue(null);
			}
			regionNameTemporalDao.create(temporal);
		}

		return region;
	}

	/**
	 * Save region name translations
	 *
	 * @param region Region to update
	 * @param temporalId	   Temporal id to apply changes for
	 * @param nameTranslations New translations
	 * @param date			 Date from which the name is valid
	 * @return updated region instance
	 * @throws FlexPayExceptionContainer exceptions container
	 */
	@Transactional (readOnly = false)
	public Region save(Region region, Long temporalId, List<RegionNameTranslation> nameTranslations,
					   Date date) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();
		Set<RegionNameTranslation> names = getTranslations(nameTranslations, container);
		RegionNameTemporal temporal = regionNameTemporalDao.readFull(temporalId);
		RegionName regionName = new RegionName();
		regionName.setTranslations(names);
		regionName.setRegion(region);
		// no changes made, return region
		if (temporal != null
			&& temporal.getBegin().equals(date)
			&& regionName.equals(temporal.getValue()) ) {
			log.info("No changes made for region name temporal");
			return region;
		}

		// no changes made to translation
		if (temporal != null && regionName.equals(temporal.getValue())) {
			regionName = temporal.getValue();
		} else {
			regionNameDao.create(regionName);
			for (RegionNameTranslation translation : names) {
				translation.setRegionName(regionName);
				translation.setId(null);
				regionNameTranslationDao.create(translation);
			}
		}

		if (temporal == null) {
			temporal = new RegionNameTemporal();
		}
		temporal.setBegin(date);
		temporal.setRegion(region);
		temporal.setValue(regionName);

		TimeLine<RegionName, RegionNameTemporal> timeLine = getTimeLine(region.getId());
		TimeLine<RegionName, RegionNameTemporal> timeLineNew =
				DateIntervalUtil.addInterval(timeLine, temporal);
		timeLine = DateIntervalUtil.invalidate(timeLine);
		saveTimeLine(timeLine);
		saveTimeLine(timeLineNew);
		region.setNamesTimeLine(timeLineNew);

		return region;
	}

	private TimeLine<RegionName, RegionNameTemporal> getTimeLine(Long regionId) {
		Region region = regionDao.readFull(regionId);
		return region.getNamesTimeLine();
	}

	private void saveTimeLine(TimeLine<RegionName, RegionNameTemporal> tl) {
		for (RegionNameTemporal temporal : tl.getIntervals()) {
			if (temporal.getId() == null) {
				regionNameTemporalDao.create(temporal);
			} else {
				regionNameTemporalDao.update(temporal);
			}
		}
	}

	private Set<RegionNameTranslation> getTranslations(List<RegionNameTranslation> nameTranslations,
													   FlexPayExceptionContainer container)
		throws FlexPayExceptionContainer {

		Set<RegionNameTranslation> names = new HashSet<RegionNameTranslation>();
		for (RegionNameTranslation nameTranslation : nameTranslations) {
			if (nameTranslation.getLang().isDefault()
				&& StringUtils.isBlank(nameTranslation.getName())) {

				FlexPayException e = new FlexPayException("No default lang translation",
						"error.region_no_default_translation");
				container.addException(e);
				continue;
			}
			if (StringUtils.isNotBlank(nameTranslation.getName())) {
				names.add(nameTranslation);
			}
		}

		if (!container.getExceptions().isEmpty()) {
			throw container;
		}

		return names;
	}

	/**
	 * Get Region name translations for specified locale, if translation is not found check
	 * for translation in default locale
	 *
	 * @param countryFilter Country filter
	 * @param pager		 Regions list pager
	 * @return List of region names
	 * @throws FlexPayException if failure occurs
	 */
	public List<RegionName> getRegionNames(CountryFilter countryFilter, Page pager)
			throws FlexPayException {

		if (log.isDebugEnabled()) {
			log.debug("Getting list of Region names: " + countryFilter);
		}

		List<Region> regions = regionDao.listRegions(pager,
				Region.STATUS_ACTIVE, countryFilter.getSelectedCountryId());
		List<RegionName> regionNames = new ArrayList<RegionName>(regions.size());

		// Get last temporal in each region names time line
		for (Region region : regions) {
			List<RegionNameTemporal> temporals = region.getNameTemporals();
			RegionNameTemporal temporal = temporals.get(temporals.size() - 1);
			regionNames.add(regionNameDao.readFull(temporal.getValue().getId()));
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
			Region reg = regionDao.read(region.getId());
			reg.setStatus(Region.STATUS_DISABLED);
			regionDao.update(reg);

			if (log.isDebugEnabled()) {
				log.debug("Disabled: " + reg);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<Long, RegionNameTranslation> getTranslations(Long temporalId) {
		RegionNameTemporal temporal = regionNameTemporalDao.readFull(temporalId);
		log.info("RegionName temporal: " + temporal);
		if (temporal == null || temporal.getValue() == null) {
			return Collections.emptyMap();
		}
		RegionName name = temporal.getValue();
		log.info("Region name translations: " + name.getTranslations());
		Map<Long, RegionNameTranslation> map = new HashMap<Long, RegionNameTranslation>();
		for (RegionNameTranslation translation : name.getTranslations()) {
			map.put(translation.getLang().getId(), translation);
		}
		return map;
	}

	/**
	 * Extract country from filter
	 *
	 * @param countryFilter Country filter
	 * @param container	 Exception container
	 * @return Countr if found or <code>null</code> otherwise
	 */
	private Country getCountry(CountryFilter countryFilter, FlexPayExceptionContainer container) {
		if (countryFilter.getSelectedCountryId() == null) {
			container.addException(new FlexPayException("null", "ab.region.no_country_selectected"));
			return null;
		}

		Country country = countryDao.read(countryFilter.getSelectedCountryId());
		if (country == null) {
			container.addException(new FlexPayException("null", "ab.region.country_id_invalid"));
			return null;
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
		return regionDao.readFull(id);
	}

	/**
	 * Read Region name temporal object by its unique id
	 *
	 * @param id Region key
	 * @return Region name temporal object, or <code>null</code> if object not found
	 */
	public RegionNameTemporal readRegionNameTemporal(Long id) {
		return regionNameTemporalDao.readFull(id);
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

	/**
	 * Setter for property 'regionNameTemporalDao'.
	 *
	 * @param regionNameTemporalDao Value to set for property 'regionNameTemporalDao'.
	 */
	public void setRegionNameTemporalDao(RegionNameTemporalDao regionNameTemporalDao) {
		this.regionNameTemporalDao = regionNameTemporalDao;
	}
}
