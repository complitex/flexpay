package org.flexpay.ab.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.TownDao;
import org.flexpay.ab.dao.TownDaoExt;
import org.flexpay.ab.dao.TownNameDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.TownService;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.NameTimeDependentDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.impl.NameTimeDependentServiceImpl;
import org.flexpay.common.service.internal.SessionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.flexpay.common.persistence.Stub.stub;

/**
 * Town service layer implementation
 */
@Transactional (readOnly = true)
public class TownServiceImpl extends NameTimeDependentServiceImpl<
		TownNameTranslation, TownName, TownNameTemporal, Town>
		implements TownService, ParentService<TownFilter> {

	private TownDao townDao;
	private TownDaoExt townDaoExt;
	private TownNameDao townNameDao;

	private ParentService<RegionFilter> parentService;
	private SessionUtils sessionUtils;
	private ModificationListener<Town> modificationListener;

	/**
	 * Read towns collection by theirs ids
	 *
 	 * @param townIds Town ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found towns
	 */
	@NotNull
	@Override
	public List<Town> readFull(@NotNull Collection<Long> townIds, boolean preserveOrder) {
		return townDao.readFullCollection(townIds, preserveOrder);
	}

	/**
	 * Disable towns
	 *
	 * @param townIds IDs of towns to disable
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(@NotNull Collection<Long> townIds) {
		for (Long id : townIds) {
			if (id == null) {
				log.warn("Null id in collection of town ids for disable");
				continue;
			}
			Town town = townDao.read(id);
			if (town == null) {
				log.warn("Can't get town with id {} from DB", id);
				continue;
			}
			town.disable();
			townDao.update(town);

			modificationListener.onDelete(town);
			log.debug("Town disabled: {}", town);
		}
	}

	/**
	 * Create town
	 *
	 * @param town Town to save
	 * @return Saved instance of town
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Town create(@NotNull Town town) throws FlexPayExceptionContainer {

		validate(town);
		town.setId(null);
		townDao.create(town);

		modificationListener.onCreate(town);

		return town;
	}

	/**
	 * Update or create town
	 *
	 * @param town Town to save
	 * @return Saved instance of town
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Town update(@NotNull Town town) throws FlexPayExceptionContainer {

		validate(town);

		Town old = readFull(stub(town));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No town found to update " + town));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, town);

		townDao.update(town);

		return town;
	}

	/**
	 * Validate town before save
	 *
	 * @param town Town object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull Town town) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (town.getRegion() == null) {
			container.addException(new FlexPayException("No region", "ab.error.town.no_region"));
		}

		TownNameTemporal nameTmprl = town.getCurrentNameTemporal();
		if (nameTmprl == null || nameTmprl.getValue() == null) {
			container.addException(new FlexPayException("No name", "ab.error.no_current_name"));
		} else {

			boolean defaultLangNameFound = false;

			for (TownNameTranslation translation : nameTmprl.getValue().getTranslations()) {

				Language lang = translation.getLang();
				String name = translation.getName();
				boolean nameNotEmpty = StringUtils.isNotEmpty(name);

				if (lang.isDefault()) {
					defaultLangNameFound = nameNotEmpty;
				}

				if (nameNotEmpty) {
					List<Town> towns = townDao.findByRegionAndNameAndLanguage(town.getParentStub().getId(), name, lang.getId());
					if (!towns.isEmpty() && !towns.get(0).getId().equals(town.getId())) {
						container.addException(new FlexPayException(
								"Name \"" + name + "\" is already use", "ab.error.name_is_already_use", name));
					}
				}

			}

			if (!defaultLangNameFound) {
				container.addException(new FlexPayException(
						"No default language translation", "ab.error.town.full_name_is_required"));
			}

		}

		if (container.isNotEmpty()) {
			throw container;
		}

	}

	/**
	 * Read town with its full hierarchical structure:
	 * country-region
	 *
	 * @param townStub Town stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	public Town readWithHierarchy(@NotNull Stub<Town> townStub) {
		List<Town> towns = townDao.findWithFullHierarchy(townStub.getId());
		return towns.isEmpty() ? null : towns.get(0);
	}

	/**
	 * Lookup towns by query and region id.
	 * Query is a string which may contains in folow string:
	 * <p/>
	 * town_name
	 *
	 * @param regionStub  Region stub
	 * @param query searching string
	 * @return List of founded regions
	 */
	@NotNull
	@Override
	public List<Town> findByParentAndQuery(@NotNull Stub<Region> regionStub, @NotNull String query) {
		return townDao.findByParentAndQuery(regionStub.getId(), query);
	}

	/**
	 * Get a list of available towns
	 *
	 * @param filters Parent filters
	 * @param sorters Stack of sorters
	 * @param pager   Page
	 * @return List of towns
	 */
	@NotNull
	@Override
	public List<Town> find(@NotNull ArrayStack filters, @NotNull List<? extends ObjectSorter> sorters, @NotNull Page<Town> pager) {
		PrimaryKeyFilter<?> regionFilter = (PrimaryKeyFilter<?>) filters.peek();
		return townDaoExt.findTowns(regionFilter.getSelectedId(), sorters, pager);
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
	public TownFilter initFilter(@Nullable TownFilter parentFilter, @NotNull PrimaryKeyFilter<?> forefatherFilter, @NotNull Locale locale)
			throws FlexPayException {

		if (parentFilter == null) {
			parentFilter = new TownFilter();
		}

		parentFilter.setNames(getTranslations(forefatherFilter, locale));

		Collection<TownNameTranslation> names = parentFilter.getNames();
		if (names.isEmpty()) {
			throw new FlexPayException("No town names", "ab.error.town.no_towns");
		}

		if (parentFilter.getSelectedId() == null || !isFilterValid(parentFilter)) {
			TownName firstObject = (TownName) names.iterator().next().getTranslatable();
			parentFilter.setSelectedId(firstObject.getObject().getId());
		}

		return parentFilter;
	}

	private boolean isFilterValid(@NotNull TownFilter filter) {
		if (!filter.needFilter()) {
			return true;
		}
		for (TownNameTranslation nameTranslation : filter.getNames()) {
			TownName name = (TownName) nameTranslation.getTranslatable();
			if (filter.getSelectedStub().sameId((Town) name.getObject())) {
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
	 * Get DAO implementation working with Name time-dependent objects
	 *
	 * @return GenericDao implementation
	 */
	@Override
	protected NameTimeDependentDao<Town, Long> getNameTimeDependentDao() {
		return townDao;
	}

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	@Override
	protected GenericDao<TownName, Long> getNameValueDao() {
		return townNameDao;
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        townDao.setJpaTemplate(jpaTemplate);
        townDaoExt.setJpaTemplate(jpaTemplate);
        townNameDao.setJpaTemplate(jpaTemplate);
        parentService.setJpaTemplate(jpaTemplate);
        sessionUtils.setJpaTemplate(jpaTemplate);
        modificationListener.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setParentService(ParentService<RegionFilter> parentService) {
		this.parentService = parentService;
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

	@Required
	public void setTownDao(TownDao townDao) {
		this.townDao = townDao;
	}

	@Required
	public void setTownNameDao(TownNameDao townNameDao) {
		this.townNameDao = townNameDao;
	}

}
