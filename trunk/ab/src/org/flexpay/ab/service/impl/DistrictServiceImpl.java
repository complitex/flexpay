package org.flexpay.ab.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.DistrictDao;
import org.flexpay.ab.dao.DistrictDaoExt;
import org.flexpay.ab.dao.DistrictNameDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.DistrictFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.dao.paging.FetchRange;
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
 * District service layer implementation
 */
@Transactional (readOnly = true)
public class DistrictServiceImpl extends NameTimeDependentServiceImpl<
		DistrictNameTranslation, DistrictName, DistrictNameTemporal, District>
		implements DistrictService, ParentService<DistrictFilter> {

	private DistrictDao districtDao;
	private DistrictDaoExt districtDaoExt;
	private DistrictNameDao districtNameDao;

	private ParentService<TownFilter> parentService;
	private SessionUtils sessionUtils;
	private ModificationListener<District> modificationListener;

	/**
	 * Read districts collection by theirs ids
	 *
 	 * @param districtIds Districts ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found districts
	 */
	@NotNull
	@Override
	public List<District> readFull(@NotNull Collection<Long> districtIds, boolean preserveOrder) {
		return districtDao.readFullCollection(districtIds, preserveOrder);
	}

	/**
	 * Disable districts
	 *
	 * @param districtIds IDs of districts to disable
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(@NotNull Collection<Long> districtIds) {
		for (Long id : districtIds) {
			if (id == null) {
				log.warn("Null id in collection of district ids for disable");
				continue;
			}
			District district = districtDao.read(id);
			if (district == null) {
				log.warn("Can't get district with id {} from DB", id);
				continue;
			}
			district.disable();
			districtDao.update(district);

			modificationListener.onDelete(district);
			log.debug("District disabled: {}", district);
		}
	}

	/**
	 * Create district
	 *
	 * @param district District to save
	 * @return Saved instance of district
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public District create(@NotNull District district) throws FlexPayExceptionContainer {

		validate(district);
		district.setId(null);
		districtDao.create(district);

		modificationListener.onCreate(district);

		return district;
	}

	/**
	 * Update or create district
	 *
	 * @param district District to save
	 * @return Saved instance of district
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public District update(@NotNull District district) throws FlexPayExceptionContainer {

		validate(district);

		District old = readFull(stub(district));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No district found to update " + district));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, district);

		districtDao.update(district);

		return district;
	}

	/**
	 * Validate district before save
	 *
	 * @param district District object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull District district) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (district.getParent() == null) {
			container.addException(new FlexPayException("No town", "ab.error.district.no_town"));
		}

		DistrictNameTemporal nameTmprl = district.getCurrentNameTemporal();
		if (nameTmprl == null || nameTmprl.getValue() == null) {
			container.addException(new FlexPayException("No name", "ab.error.no_current_name"));
		} else {

			boolean defaultLangNameFound = false;

			for (DistrictNameTranslation translation : nameTmprl.getValue().getTranslations()) {

				Language lang = translation.getLang();
				String name = translation.getName();
				boolean nameNotEmpty = StringUtils.isNotEmpty(name);

				if (lang.isDefault()) {
					defaultLangNameFound = nameNotEmpty;
				}

				if (nameNotEmpty) {
					List<District> districts = districtDao.findByTownAndNameAndLanguage(district.getParentStub().getId(), name, lang.getId());
					if (!districts.isEmpty() && !districts.get(0).getId().equals(district.getId())) {
						container.addException(new FlexPayException(
								"Name '" + name + "' is already use", "ab.error.name_is_already_use", name));
					}
				}

			}

			if (!defaultLangNameFound) {
				container.addException(new FlexPayException(
						"No default language translation", "ab.error.district.full_name_is_required"));
			}

		}

		if (container.isNotEmpty()) {
			throw container;
		}

	}

	/**
	 * Read district with its full hierarchical structure:
	 * country-region-town
	 *
	 * @param districtStub District stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	public District readWithHierarchy(@NotNull Stub<District> districtStub) {
		List<District> districts = districtDao.findWithFullHierarchy(districtStub.getId());
		return districts.isEmpty() ? null : districts.get(0);
	}

	/**
	 * Lookup districts by query and town id.
	 * Query is a string which may contains in folow string:
	 * <p/>
	 * district_name
	 *
	 * @param parentStub  Town stub
	 * @param query searching string
	 * @return List of founded districts
	 */
	@NotNull
	@Override
	public List<District> findByParentAndQuery(@NotNull Stub<Town> parentStub, @NotNull String query) {
		return districtDao.findByParentAndQuery(parentStub.getId(), query);
	}

	/**
	 * Get a list of available districts
	 *
	 * @param filters Parent filters
	 * @param sorters Stack of sorters
	 * @param pager   Page
	 * @return List of districts
	 */
	@NotNull
	@Override
	public List<District> find(@NotNull ArrayStack filters, @NotNull List<? extends ObjectSorter> sorters, @NotNull Page<District> pager) {
		PrimaryKeyFilter<?> townFilter = (PrimaryKeyFilter<?>) filters.peek();
		return districtDaoExt.findDistricts(townFilter.getSelectedId(), sorters, pager);
	}

	/**
	 * Lookup districts by name and town
	 *
	 * @param townStub Town stub
	 * @param name District name search string
	 * @return List of found districts
	 */
	@NotNull
	@Override
	public List<District> findByTownAndName(@NotNull Stub<Town> townStub, @NotNull String name) {
		return districtDao.findByTownAndName(townStub.getId(), name);
	}

	@NotNull
	@Override
	public List<District> findSimpleByTown(Stub<Town> townStub, FetchRange range) {
		return districtDao.findSimpleByTown(townStub.getId(), range);
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
	public DistrictFilter initFilter(@Nullable DistrictFilter parentFilter, @NotNull PrimaryKeyFilter<?> forefatherFilter, @NotNull Locale locale)
			throws FlexPayException {

		if (parentFilter == null) {
			parentFilter = new DistrictFilter();
		}

		parentFilter.setNames(getTranslations(forefatherFilter, locale));

		Collection<DistrictNameTranslation> names = parentFilter.getNames();
		if (names.isEmpty()) {
			throw new FlexPayException("No district names", "ab.error.district.no_districts");
		}

		if (parentFilter.getSelectedId() == null || !isFilterValid(parentFilter)) {
			DistrictName firstObject = (DistrictName) names.iterator().next().getTranslatable();
			parentFilter.setSelectedId(firstObject.getObject().getId());
		}

		return parentFilter;

	}

	private boolean isFilterValid(@NotNull DistrictFilter filter) {
		if (!filter.needFilter()) {
			return true;
		}
		for (DistrictNameTranslation nameTranslation : filter.getNames()) {
			DistrictName name = (DistrictName) nameTranslation.getTranslatable();
			if (filter.getSelectedStub().sameId((District) name.getObject())) {
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

		DistrictFilter parentFilter = filters.isEmpty() ? null : (DistrictFilter) filters.pop();
		filters = parentService.initFilters(filters, locale);
		TownFilter forefatherFilter = (TownFilter) filters.peek();

		// init town filter
		parentFilter = initFilter(parentFilter, forefatherFilter, locale);
		filters.push(parentFilter);

		return filters;
	}

	/**
	 * Get DAO implementation working with Name time-dependent objects
	 *
	 * @return GenericDao implementation
	 */
	@Override
	protected DistrictDao getNameTimeDependentDao() {
		return districtDao;
	}

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	@Override
	protected DistrictNameDao getNameValueDao() {
		return districtNameDao;
	}

	@Required
	public void setDistrictDao(DistrictDao districtDao) {
		this.districtDao = districtDao;
	}

	@Required
	public void setDistrictNameDao(DistrictNameDao districtNameDao) {
		this.districtNameDao = districtNameDao;
	}

	@Required
	public void setParentService(ParentService<TownFilter> parentService) {
		this.parentService = parentService;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<District> modificationListener) {
		this.modificationListener = modificationListener;
	}

	@Required
	public void setDistrictDaoExt(DistrictDaoExt districtDaoExt) {
		this.districtDaoExt = districtDaoExt;
	}

}
