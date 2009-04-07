package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.dao.*;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.ab.service.RegionService;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.imp.NameTimeDependentServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Locale;
import java.util.List;

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

	private ParentService<CountryFilter> parentService;
	private TownTypeService townTypeService;

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
	 * return base for name time-dependent objects in i18n files, like 'region', 'town', etc.
	 *
	 * @return Localization key base
	 */
	protected String getI18nKeyBase() {
		return "ab.region";
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
	public ArrayStack initFilters(ArrayStack filters, Locale locale)
			throws FlexPayException {
		if (filters == null) {
			filters = new ArrayStack();
		}

		TownTypeFilter townTypeFilter = null;
		if (!filters.isEmpty() && filters.peek() instanceof TownTypeFilter) {
			townTypeFilter = (TownTypeFilter) filters.pop();
		}

		RegionFilter parentFilter = filters.isEmpty() ? null : (RegionFilter) filters.pop();
		filters = parentService.initFilters(filters, locale);
		CountryFilter forefatherFilter = (CountryFilter) filters.peek();

		// init region filter
		parentFilter = initFilter(parentFilter, forefatherFilter, locale);
		filters.push(parentFilter);

		if (townTypeFilter != null) {
			townTypeFilter = townTypeService.initFilter(townTypeFilter, locale);
			filters.push(townTypeFilter);
		}

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
		Collection<RegionNameTranslation> names = parentFilter.getNames();
		if (names.isEmpty()) {
			throw new FlexPayException("No region names", "ab.no_regions");
		}

		if (parentFilter.getSelectedId() == null || !isFilterValid(parentFilter)) {
			RegionName firstObject = (RegionName) names.iterator().next().getTranslatable();
			parentFilter.setSelectedId(firstObject.getObject().getId());
		}

		return parentFilter;
	}

	private boolean isFilterValid(RegionFilter filter) {
		for (RegionNameTranslation nameTranslation : filter.getNames()) {
			RegionName regionName = (RegionName) nameTranslation.getTranslatable();
			if (regionName.getObject().getId().equals(filter.getSelectedId())) {
				return true;
			}
		}

		return false;
	}

	@NotNull
	public List<Region> findByCountryAndQuery(@NotNull Stub<Country> stub, @NotNull String query) {
		return regionDao.findByCountryAndQuery(stub.getId(), query);
	}

	@Required
	public void setParentService(ParentService<CountryFilter> parentService) {
		this.parentService = parentService;
	}

	@Required
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

	@Required
	public void setRegionDao(RegionDao regionDao) {
		this.regionDao = regionDao;
	}

	@Required
	public void setRegionNameDao(RegionNameDao regionNameDao) {
		this.regionNameDao = regionNameDao;
	}

	@Required
	public void setCountryDao(CountryDao countryDao) {
		this.countryDao = countryDao;
	}

	@Required
	public void setRegionNameTranslationDao(RegionNameTranslationDao regionNameTranslationDao) {
		this.regionNameTranslationDao = regionNameTranslationDao;
	}

	@Required
	public void setRegionNameTemporalDao(RegionNameTemporalDao regionNameTemporalDao) {
		this.regionNameTemporalDao = regionNameTemporalDao;
	}

}
