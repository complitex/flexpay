package org.flexpay.ab.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.*;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.persistence.filters.TownTypeFilter;
import org.flexpay.ab.service.TownService;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.NameTimeDependentDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.impl.NameTimeDependentServiceImpl;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class TownServiceImpl extends NameTimeDependentServiceImpl<
		TownNameTranslation, TownName, TownNameTemporal, Town, Region>
		implements TownService {

	private TownDao townDao;
	private TownDaoExt townDaoExt;
	private TownNameDao townNameDao;
	private TownNameTemporalDao townNameTemporalDao;
	private RegionDao regionDao;

	private ParentService<RegionFilter> parentService;
	private TownTypeService townTypeService;

	private SessionUtils sessionUtils;
	private ModificationListener<Town> modificationListener;

	@Required
	public void setTownDao(TownDao townDao) {
		this.townDao = townDao;
	}

	@Required
	public void setTownNameDao(TownNameDao townNameDao) {
		this.townNameDao = townNameDao;
	}

	/**
	 * return base for name time-dependent objects in i18n files, like 'region', 'town', etc.
	 *
	 * @return Localization key base
	 */
	protected String getI18nKeyBase() {
		return "ab.town";
	}

	/**
	 * Get DAO implementation working with Name time-dependent objects
	 *
	 * @return GenericDao implementation
	 */
	protected NameTimeDependentDao<Town, Long> getNameTimeDependentDao() {
		return townDao;
	}

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	protected GenericDao<TownNameTemporal, Long> getNameTemporalDao() {
		return townNameTemporalDao;
	}

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	protected GenericDao<TownName, Long> getNameValueDao() {
		return townNameDao;
	}

	/**
	 * Get DAO implementation working with parent objects
	 *
	 * @return GenericDao implementation
	 */
	protected GenericDao<Region, Long> getParentDao() {
		return regionDao;
	}

	/**
	 * Check if disable operation on object is allowed
	 *
	 * @param town	  Name time dependent object
	 * @param container Exceptions container to add exception for
	 * @return <code>true</code> if operation allowed, or <code>false</otherwise>
	 */
	protected boolean canDisable(Town town, FlexPayExceptionContainer container) {
		return true;
	}

	/**
	 * Getter for property 'newNameTemporal'.
	 *
	 * @return Value for property 'newNameTemporal'.
	 */
	protected TownNameTemporal getNewNameTemporal() {
		return new TownNameTemporal();
	}

	/**
	 * Getter for property 'newNameTimeDependent'.
	 *
	 * @return Value for property 'newNameTimeDependent'.
	 */
	protected Town getNewNameTimeDependent() {
		return new Town();
	}

	/**
	 * Getter for property 'emptyName'.
	 *
	 * @return Value for property 'emptyName'.
	 */
	protected TownName getEmptyName() {
		return new TownName();
	}

	/**
	 * Getter for property 'emptyType'.
	 *
	 * @return Value for property 'emptyType'.
	 */
	protected TownType getEmptyType() {
		return new TownType();
	}


	/**
	 * Create empty name translation
	 *
	 * @return name translation
	 */
	public TownNameTranslation getEmptyNameTranslation() {
		return new TownNameTranslation();
	}

	/**
	 * {@inheritDoc}
	 */
	public TownFilter initFilter(TownFilter parentFilter, PrimaryKeyFilter forefatherFilter, Locale locale)
			throws FlexPayException {

		if (parentFilter == null) {
			parentFilter = new TownFilter();
		}

		parentFilter.setNames(getTranslations(forefatherFilter, locale));

		Collection<TownNameTranslation> names = parentFilter.getNames();
		if (names.isEmpty()) {
			throw new FlexPayException("No town names", "ab.no_towns");
		}
		if (parentFilter.getSelectedId() == null || !isFilterValid(parentFilter)) {
			TownName firstObject = (TownName) names.iterator().next().getTranslatable();
			parentFilter.setSelectedId(firstObject.getObject().getId());
		}

		return parentFilter;
	}

	private boolean isFilterValid(TownFilter filter) {
		for (TownNameTranslation nameTranslation : filter.getNames()) {
			TownName name = (TownName) nameTranslation.getTranslatable();
			if (filter.getSelectedStub().sameId((Town) name.getObject())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public ArrayStack initFilters(ArrayStack filters, Locale locale) throws FlexPayException {
		if (filters == null) {
			filters = new ArrayStack();
		}

		// skip unknown filters if any
		ArrayStack skippedFilters = new ArrayStack();
		while (!filters.empty()) {
			ObjectFilter filter = (ObjectFilter) filters.peek();
			if (filter instanceof TownFilter) {
				break;
			}
			skippedFilters.push(filters.pop());
		}

		TownFilter parentFilter = filters.isEmpty() ? null : (TownFilter) filters.pop();
		filters = parentService.initFilters(filters, locale);
		RegionFilter forefatherFilter = (RegionFilter) filters.peek();

		// init region filter
		parentFilter = initFilter(parentFilter, forefatherFilter, locale);
		filters.push(parentFilter);

		// now add skipped filters
		while (!skippedFilters.empty()) {
			filters.push(skippedFilters.pop());
		}

		return filters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public Town create(Town object, List<TownNameTranslation> nameTranslations, ArrayStack filters, Date date) throws FlexPayExceptionContainer {

		TownTypeFilter filter = (TownTypeFilter) filters.pop();
		log.info("type filter: {}", filter);
		Town typable = object == null ? getNewNameTimeDependent() : object;

		TownType type = townTypeService.read(filter.getSelectedStub());
		TownTypeTemporal typeTemporal = new TownTypeTemporal();
		typeTemporal.setValue(type);
		typeTemporal.setTown(typable);
		TimeLine<TownType, TownTypeTemporal> typesTimeLine = new TimeLine<TownType, TownTypeTemporal>(typeTemporal);

		typable.setTypesTimeLine(typesTimeLine);

		return super.create(typable, nameTranslations, filters, date);
	}

	/**
	 * Run any post create actions on object
	 *
	 * @param object Persisted object
	 * @return The object itself
	 * @throws FlexPayExceptionContainer if failure occurs
	 */
	@Override
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public Town postCreate(Town object) throws FlexPayExceptionContainer {
		for (TownTypeTemporal typeTemporal : object.getTypeTemporals()) {
			TownType empty = getEmptyType();
			typeTemporal.setTown(object);
			if (typeTemporal.getValue().equals(empty)) {
				typeTemporal.setValue(null);
			}
		}

		return object;
	}

	/**
	 * Create Town object
	 *
	 * @param town Town object to save
	 * @return saved object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	public Town create(@NotNull Town town) throws FlexPayExceptionContainer {

		validate(town);
		town.setId(null);
		townDao.create(town);

		modificationListener.onCreate(town);

		return town;
	}

	/**
	 * Create or update Town object
	 *
	 * @param town Town object to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	public Town update(@NotNull Town town) throws FlexPayExceptionContainer {
		validate(town);

		Town old = readFull(stub(town));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No object found to update " + town));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, town);

		townDao.update(town);

		return town;
	}

	/**
	 * Disable NTD
	 *
	 * @param objects NTDs to disable
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(Collection<Town> objects) {

		log.info("{} objects to disable", objects.size());

		for (Town town : objects) {
			Town townPersisted = readFull(stub(town));
			if (townPersisted == null) {
				log.info("Requested town delete, but not found {}", town);
				continue;
			}
			townPersisted.disable();
			townDao.update(townPersisted);

			modificationListener.onDelete(townPersisted);

			log.debug("Disabled town: {}", townPersisted);
		}
	}

	/**
	 * Disable towns
	 *
	 * @param objectIds Towns identifiers
	 */
	@Transactional (readOnly = false)
	@Override
	public void disableByIds(@NotNull Collection<Long> objectIds) {
		for (Long id : objectIds) {
			Town town = townDao.read(id);
			if (town != null) {
				town.disable();
				townDao.update(town);

				modificationListener.onDelete(town);
				log.debug("Disabled: {}", town);
			}
		}
	}

	@NotNull
	public List<Town> findByRegionAndQuery(@NotNull Stub<Region> stub, @NotNull String query) {
		return townDao.findByRegionAndQuery(stub.getId(), query);
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull Town town) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		TownNameTemporal nameTmprl = town.getCurrentNameTemporal();
		if (nameTmprl == null || nameTmprl.getValue() == null) {
			ex.addException(new FlexPayException("No name", "ab.error.no_current_name"));
		} else {
			validate(nameTmprl.getValue(), ex);
		}

		TownTypeTemporal typeTmprl = town.getCurrentTypeTemporal();
		if (typeTmprl == null || typeTmprl.getValue() == null) {
			ex.addException(new FlexPayException("No type", "ab.error.no_current_type"));
		}

		if (town.getRegion() == null) {
			ex.addException(new FlexPayException("No region", "ab.error.town.no_region"));
		}

		if (ex.isNotEmpty()) {
			ex.debug(log);
			throw ex;
		}
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull TownName townName, @NotNull FlexPayExceptionContainer ex) {

		boolean defaultLangTranslationFound = false;
		for (TownNameTranslation translation : townName.getTranslations()) {
			if (translation.getLang().isDefault() && StringUtils.isNotEmpty(translation.getName())) {
				defaultLangTranslationFound = true;
			}
		}

		if (!defaultLangTranslationFound) {
			ex.addException(new FlexPayException("No default translation", "error.no_default_translation"));
		}
	}

	@NotNull
	@Override
	public List<Town> find(ArrayStack filters, List<ObjectSorter> sorters, Page<Town> pager) {

		log.debug("Finding towns with sorters");
		PrimaryKeyFilter<?> regionFilter = (PrimaryKeyFilter<?>) filters.peek();
		return townDaoExt.findTowns(regionFilter.getSelectedId(), sorters, pager);
	}

	/**
	 * Read towns
	 *
	 * @param stubs		 town keys
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Objects if found, or <code>null</code> otherwise
	 */
	@NotNull
	@Override
	public List<Town> readFull(@NotNull Collection<Long> stubs, boolean preserveOrder) {

		List<Town> towns = townDao.readFullCollection(stubs, preserveOrder);
		// merge town types
		List<Town> townTypes = townDao.findFullTownsWithTypes(stubs);
		Map<Long, Town> map = CollectionUtils.map();
		for (Town t : townTypes) {
			map.put(t.getId(), t);
		}
		for (Town town : towns) {
			if (map.containsKey(town.getId())) {
				town.setTypeTemporals(map.get(town.getId()).getTypeTemporals());
			}
		}

		return towns;
	}

	@Required
	public void setTownNameTemporalDao(TownNameTemporalDao townNameTemporalDao) {
		this.townNameTemporalDao = townNameTemporalDao;
	}

	@Required
	public void setRegionDao(RegionDao regionDao) {
		this.regionDao = regionDao;
	}

	@Required
	public void setParentService(ParentService<RegionFilter> parentService) {
		this.parentService = parentService;
	}

	@Required
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

	@Required
	public void setModificationListener(ModificationListener<Town> modificationListener) {
		this.modificationListener = modificationListener;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setTownDaoExt(TownDaoExt townDaoExt) {
		this.townDaoExt = townDaoExt;
	}
}
