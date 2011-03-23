package org.flexpay.ab.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.RegionDao;
import org.flexpay.ab.dao.RegionDaoExt;
import org.flexpay.ab.dao.RegionNameDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.ab.service.RegionService;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.impl.NameTimeDependentServiceImpl;
import org.flexpay.common.service.internal.SessionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.flexpay.common.persistence.Stub.stub;

/**
 * Region service layer implementation
 */
@Transactional (readOnly = true)
public class RegionServiceImpl extends NameTimeDependentServiceImpl<
		RegionNameTranslation, RegionName, RegionNameTemporal, Region>
		implements RegionService, ParentService<RegionFilter> {

	private RegionDao regionDao;
	private RegionDaoExt regionDaoExt;
	private RegionNameDao regionNameDao;

	private ParentService<CountryFilter> parentService;
	private TownTypeService townTypeService;
	private SessionUtils sessionUtils;
	private ModificationListener<Region> modificationListener;

	/**
	 * Read regions collection by theirs ids
	 *
 	 * @param regionIds Region ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found regions
	 */
	@NotNull
	@Override
	public List<Region> readFull(@NotNull Collection<Long> regionIds, boolean preserveOrder) {
		return regionDao.readFullCollection(regionIds, preserveOrder);
	}

	/**
	 * Disable regions
	 *
	 * @param regionIds IDs of regions to disable
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(@NotNull Collection<Long> regionIds) {
		for (Long id : regionIds) {
			if (id == null) {
				log.warn("Null id in collection of region ids for disable");
				continue;
			}
			Region region = regionDao.read(id);
			if (region == null) {
				log.warn("Can't get region with id {} from DB", id);
				continue;
			}
			region.disable();
			regionDao.update(region);

			modificationListener.onDelete(region);
			log.debug("Region disabled: {}", region);
		}
	}

	/**
	 * Create region
	 *
	 * @param region Region to save
	 * @return Saved instance of region
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Region create(@NotNull Region region) throws FlexPayExceptionContainer {

		validate(region);
		region.setId(null);
		regionDao.create(region);

		modificationListener.onCreate(region);

		return region;
	}

	/**
	 * Update or create region
	 *
	 * @param region Region to save
	 * @return Saved instance of region
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Region update(@NotNull Region region) throws FlexPayExceptionContainer {

		validate(region);

		Region old = readFull(stub(region));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No region found to update " + region));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, region);

		regionDao.update(region);

		return region;
	}

	/**
	 * Validate region before save
	 *
	 * @param region Region object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull Region region) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		RegionNameTemporal nameTmprl = region.getCurrentNameTemporal();
		if (nameTmprl == null || nameTmprl.getValue() == null) {
			container.addException(new FlexPayException("No name", "ab.error.no_current_name"));
		} else {

			boolean defaultLangNameFound = false;

			for (RegionNameTranslation translation : nameTmprl.getValue().getTranslations()) {

				Language lang = translation.getLang();
				String name = translation.getName();
				boolean nameNotEmpty = StringUtils.isNotEmpty(name);

				if (lang.isDefault()) {
					defaultLangNameFound = nameNotEmpty;
				}

				if (nameNotEmpty) {
					List<Region> regions = regionDao.findByCountryAndNameAndLanguage(region.getParentStub().getId(), name, lang.getId());
					if (!regions.isEmpty() && !regions.get(0).getId().equals(region.getId())) {
						container.addException(new FlexPayException(
								"Name \"" + name + "\" is already use", "ab.error.name_is_already_use", name));
					}
				}

			}

			if (!defaultLangNameFound) {
				container.addException(new FlexPayException(
						"No default language translation", "ab.error.region.full_name_is_required"));
			}

		}

		if (region.getCountry() == null) {
			container.addException(new FlexPayException("No country", "ab.error.region.no_country"));
		}

		if (container.isNotEmpty()) {
			throw container;
		}

	}

	/**
	 * Lookup regions by query and country id.
	 * Query is a string which may contains in folow string:
	 * <p/>
	 * region_name
	 *
	 * @param parentStub  Country stub
	 * @param query searching string
	 * @return List of founded regions
	 */
	@NotNull
	@Override
	public List<Region> findByParentAndQuery(@NotNull Stub<Country> parentStub, @NotNull String query) {
		return regionDao.findByParentAndQuery(parentStub.getId(), query.toUpperCase());
	}

	/**
	 * Get a list of available regions
	 *
	 * @param filters Parent filters
	 * @param sorters Stack of sorters
	 * @param pager   Page
	 * @return List of regions
	 */
	@NotNull
	@Override
	public List<Region> find(@NotNull ArrayStack filters, @NotNull List<? extends ObjectSorter> sorters, @NotNull Page<Region> pager) {
		PrimaryKeyFilter<?> countryFilter = (PrimaryKeyFilter<?>) filters.peek();
		return regionDaoExt.findRegions(countryFilter.getSelectedId(), sorters, pager);
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
	@NotNull
	@Override
	public RegionFilter initFilter(@Nullable RegionFilter parentFilter, @NotNull PrimaryKeyFilter<?> forefatherFilter, @NotNull Locale locale)
			throws FlexPayException {

		if (parentFilter == null) {
			parentFilter = new RegionFilter();
		}

		parentFilter.setNames(getTranslations(forefatherFilter, locale));

		Collection<RegionNameTranslation> names = parentFilter.getNames();
		if (names.isEmpty()) {
			throw new FlexPayException("No region names", "ab.error.region.no_regions");
		}

		if (parentFilter.getSelectedId() == null || !isFilterValid(parentFilter)) {
			RegionName firstObject = (RegionName) names.iterator().next().getTranslatable();
			parentFilter.setSelectedId(firstObject.getObject().getId());
		}

		return parentFilter;
	}

	private boolean isFilterValid(@NotNull RegionFilter filter) {
		if (!filter.needFilter()) {
			return true;
		}
		for (RegionNameTranslation nameTranslation : filter.getNames()) {
			RegionName regionName = (RegionName) nameTranslation.getTranslatable();
			if (filter.getSelectedStub().sameId((Region) regionName.getObject())) {
				return true;
			}
		}

		return false;
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

		TownTypeFilter townTypeFilter = null;
		if (!filters.isEmpty() && filters.peek() instanceof TownTypeFilter) {
			townTypeFilter = (TownTypeFilter) filters.pop();
		}

		RegionFilter parentFilter = filters.isEmpty() ? null : (RegionFilter) filters.pop();
		filters = parentService.initFilters(filters, locale);
		CountryFilter forefatherFilter = (CountryFilter) filters.peek();

		// init country filter
		parentFilter = initFilter(parentFilter, forefatherFilter, locale);
		filters.push(parentFilter);

		if (townTypeFilter != null) {
			townTypeFilter = townTypeService.initFilter(townTypeFilter, locale);
			filters.push(townTypeFilter);
		}

		return filters;
	}

	/**
	 * Get DAO implementation working with Name time-dependent objects
	 *
	 * @return GenericDao implementation
	 */
	@Override
	protected RegionDao getNameTimeDependentDao() {
		return regionDao;
	}

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	@Override
	protected RegionNameDao getNameValueDao() {
		return regionNameDao;
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
