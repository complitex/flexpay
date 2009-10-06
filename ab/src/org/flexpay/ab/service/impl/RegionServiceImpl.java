package org.flexpay.ab.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.*;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.ab.service.RegionService;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.impl.NameTimeDependentServiceImpl;
import org.flexpay.common.service.internal.SessionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Region service layer implementation
 */
@Transactional (readOnly = true, rollbackFor = Exception.class)
public class RegionServiceImpl extends NameTimeDependentServiceImpl<
		RegionNameTranslation, RegionName, RegionNameTemporal, Region, Country>
		implements RegionService {

	private RegionDao regionDao;
	private RegionDaoExt regionDaoExt;
	private RegionNameDao regionNameDao;
	private RegionNameTemporalDao regionNameTemporalDao;
	private CountryDao countryDao;

	private ParentService<CountryFilter> parentService;
	private TownTypeService townTypeService;
	private SessionUtils sessionUtils;
	private ModificationListener<Region> modificationListener;

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

	@Transactional (readOnly = false)
	public Region create(@NotNull Region region) throws FlexPayExceptionContainer {

		validate(region);
		region.setId(null);
		regionDao.create(region);

		modificationListener.onCreate(region);

		return region;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	public Region update(@NotNull Region region) throws FlexPayExceptionContainer {
		validate(region);

		Region old = readFull(stub(region));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No object found to update " + region));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, region);

		regionDao.update(region);

		return region;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull Region region) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		RegionNameTemporal nameTmprl = region.getCurrentNameTemporal();
		if (nameTmprl == null || nameTmprl.getValue() == null) {
			ex.addException(new FlexPayException("No name", "ab.error.no_current_name"));
		} else {
			validate(nameTmprl.getValue(), ex);
		}

		if (region.getCountry() == null) {
			ex.addException(new FlexPayException("No country", "ab.error.region.no_country"));
		}

		if (ex.isNotEmpty()) {
			ex.debug(log);
			throw ex;
		}
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull RegionName regionName, @NotNull FlexPayExceptionContainer ex) {

		boolean defaultLangTranslationFound = false;
		for (RegionNameTranslation translation : regionName.getTranslations()) {
			if (translation.getLang().isDefault() && StringUtils.isNotEmpty(translation.getName())) {
				defaultLangTranslationFound = true;
			}
		}

		if (!defaultLangTranslationFound) {
			ex.addException(new FlexPayException("No default translation", "error.no_default_translation"));
		}
	}

	/**
	 * Disable regions
	 *
	 * @param objectIds Regions identifiers
	 */
	@Transactional (readOnly = false)
	@Override
	public void disableByIds(@NotNull Collection<Long> objectIds) {
		for (Long id : objectIds) {
			Region region = regionDao.read(id);
			if (region != null) {
				region.disable();
				regionDao.update(region);

				modificationListener.onDelete(region);
				log.debug("Disabled: {}", region);
			}
		}
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
			if (filter.getSelectedStub().sameId((Region) regionName.getObject())) {
				return true;
			}
		}

		return false;
	}

	@NotNull
	@Override
	public List<Region> find(ArrayStack filters, List<ObjectSorter> sorters, Page<Region> pager) {

		log.debug("Finding regions with sorters");
		PrimaryKeyFilter<?> countryFilter = (PrimaryKeyFilter<?>) filters.peek();
		return regionDaoExt.findRegions(countryFilter.getSelectedId(), sorters, pager);
	}

	/**
	 * Read regions
	 *
	 * @param stubs		 Region keys
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Objects if found, or <code>null</code> otherwise
	 */
	@NotNull
	@Override
	public List<Region> readFull(@NotNull Collection<Long> stubs, boolean preserveOrder) {
		return regionDao.readFullCollection(stubs, preserveOrder);
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
	public void setRegionNameTemporalDao(RegionNameTemporalDao regionNameTemporalDao) {
		this.regionNameTemporalDao = regionNameTemporalDao;
	}

	@Required
	public void setModificationListener(ModificationListener<Region> modificationListener) {
		this.modificationListener = modificationListener;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setRegionDaoExt(RegionDaoExt regionDaoExt) {
		this.regionDaoExt = regionDaoExt;
	}
}
