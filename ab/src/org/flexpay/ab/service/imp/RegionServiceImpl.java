package org.flexpay.ab.service.imp;

import org.flexpay.ab.dao.*;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.service.RegionService;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.service.imp.NameTimeDependentServiceImpl;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.DomainObject;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Locale;
import java.util.ArrayList;

/**
 * Region service layer implementation
 */
@Transactional (readOnly = true, rollbackFor = Exception.class)
public class RegionServiceImpl extends NameTimeDependentServiceImpl<
		RegionNameTranslation, RegionName, RegionNameTemporal, Region, Country>
		implements RegionService {

	private RegionDao regionDao;
	private RegionNameDao regionNameDao;
	private RegionNameTemporalDao regionNameTemporalDao;
	private RegionNameTranslationDao regionNameTranslationDao;
	private CountryDao countryDao;

	private ParentService<CountryNameTranslation, CountryFilter> parentService;

	/**
	 * TODO CHECK if region has any towns
	 *
	 * @param region	Region to check
	 * @param container FlexPayExceptionContainer
	 * @return <code>true</code>
	 */
	protected boolean canDisable(Region region, FlexPayExceptionContainer container) {
		return true;
	}

	protected RegionDao getNameTimeDependentDao() {
		return regionDao;
	}

	protected RegionNameTemporalDao getNameTemporalDao() {
		return regionNameTemporalDao;
	}

	/**
	 * Setter for property 'parentService'.
	 *
	 * @param parentService Value to set for property 'parentService'.
	 */
	public void setParentService(ParentService<CountryNameTranslation, CountryFilter> parentService) {
		this.parentService = parentService;
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

	/**
	 * return base for name time-dependent objects in i18n files, like 'region', 'town',
	 * etc.
	 *
	 * @return Localization key base
	 */
	protected String getI18nKeyBase() {
		return "region";
	}

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	protected RegionNameDao getNameValueDao() {
		return regionNameDao;
	}

	/**
	 * Get DAO implementation working with name translations
	 *
	 * @return GenericDao implementation
	 */
	protected RegionNameTranslationDao getNameTranslationDao() {
		return regionNameTranslationDao;
	}

	/**
	 * Get DAO implementation working with parent objects
	 *
	 * @return GenericDao implementation
	 */
	protected GenericDao<Country, Long> getParentDao() {
		return countryDao;
	}

	protected RegionNameTemporal getNewNameTemporal() {
		return new RegionNameTemporal();
	}

	protected Region getNewNameTimeDependent() {
		return new Region();
	}

	protected RegionName getEmptyName() {
		return new RegionName();
	}

	public RegionNameTranslation getEmptyNameTranslation() {
		return new RegionNameTranslation();
	}

	/**
	 * Initialize filters
	 *
	 * @param filters Filters to init
	 * @param locale  Locale to get parent names in
	 * @return Initialised filters collection
	 */
	public Collection<PrimaryKeyFilter> initFilters(Collection<PrimaryKeyFilter> filters, Locale locale)
			throws FlexPayException {
		if (filters == null) {
			filters = new ArrayList<PrimaryKeyFilter>();
		}
		PrimaryKeyFilter[] filtersArr = filters.toArray(new PrimaryKeyFilter[filters.size()]);

		// init country filter
		CountryFilter countryFilter = filtersArr.length == 0 ?
									  null : (CountryFilter) filtersArr[0];
		countryFilter = parentService.initFilter(countryFilter, null, locale);

		// init region filter
		RegionFilter regionFilter = filtersArr.length == 0 ?
									null : (RegionFilter) filtersArr[1];
		regionFilter = initFilter(regionFilter, countryFilter, locale);

		filters.clear();
		filters.add(countryFilter);
		filters.add(regionFilter);

		return filters;
	}

	/**
	 * Initialize parent filter. Possibly taking in account upper level forefather filter
	 *
	 * @param parentFilter	 Filter to init
	 * @param forefatherFilter Upper level filter
	 * @param locale		   Locale to get parent names in
	 * @return Initialised filter
	 * @throws FlexPayException if failure occurs
	 */
	public RegionFilter initFilter(RegionFilter parentFilter, PrimaryKeyFilter forefatherFilter, Locale locale)
			throws FlexPayException {
		if (parentFilter == null) {
			parentFilter = new RegionFilter();
		}

		parentFilter.setNames(getTranslations(forefatherFilter, locale));
		if (parentFilter.getSelectedId() == null) {
			Collection<RegionNameTranslation> names = parentFilter.getNames();
			if (names.isEmpty()) {
				throw new FlexPayException("No region names", "ab.no_regions");
			}

			DomainObject firstObject = names.iterator().next().getTranslatable();
			parentFilter.setSelectedId(firstObject.getId());
		}

		return parentFilter;
	}
}
