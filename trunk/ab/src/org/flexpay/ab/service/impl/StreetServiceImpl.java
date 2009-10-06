package org.flexpay.ab.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.*;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.persistence.filters.StreetNameFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.NameTimeDependentDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.impl.NameTimeDependentServiceImpl;
import org.flexpay.common.service.internal.SessionUtils;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional (readOnly = true)
public class StreetServiceImpl extends NameTimeDependentServiceImpl<
		StreetNameTranslation, StreetName, StreetNameTemporal, Street, Town>
		implements StreetService {

	private StreetDao streetDao;
	private StreetDaoExt streetDaoExt;
	private StreetNameDao streetNameDao;
	private StreetNameTemporalDao streetNameTemporalDao;
	private TownDao townDao;

	private ParentService<TownFilter> parentService;

	private SessionUtils sessionUtils;
	private ModificationListener<Street> modificationListener;

	/**
	 * Get DAO implementation working with Name time-dependent objects
	 *
	 * @return GenericDao implementation
	 */
	@Override
	protected NameTimeDependentDao<Street, Long> getNameTimeDependentDao() {
		return streetDao;
	}

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	@Override
	protected GenericDao<StreetNameTemporal, Long> getNameTemporalDao() {
		return streetNameTemporalDao;
	}

	/**
	 * Get DAO implementation working with DateIntervals
	 *
	 * @return GenericDao implementation
	 */
	@Override
	protected GenericDao<StreetName, Long> getNameValueDao() {
		return streetNameDao;
	}

	/**
	 * Get DAO implementation working with parent objects
	 *
	 * @return GenericDao implementation
	 */
	@Override
	protected GenericDao<Town, Long> getParentDao() {
		return townDao;
	}

	/**
	 * Getter for property 'newNameTemporal'.
	 *
	 * @return Value for property 'newNameTemporal'.
	 */
	@Override
	protected StreetNameTemporal getNewNameTemporal() {
		return new StreetNameTemporal();
	}

	/**
	 * Getter for property 'newNameTimeDependent'.
	 *
	 * @return Value for property 'newNameTimeDependent'.
	 */
	@Override
	protected Street getNewNameTimeDependent() {
		return new Street();
	}

	/**
	 * Getter for property 'emptyName'.
	 *
	 * @return Value for property 'emptyName'.
	 */
	@Override
	protected StreetName getEmptyName() {
		return new StreetName();
	}

	/**
	 * Check if disable operation on object is allowed
	 *
	 * @param street	Name time dependent object
	 * @param container Exceptions container to add exception for
	 * @return <code>true</code> if operation allowed, or <code>false</otherwise>
	 */
	@Override
	protected boolean canDisable(Street street, FlexPayExceptionContainer container) {
		return true;
	}

	/**
	 * return base for name time-dependent objects in i18n files, like 'region', 'town', etc.
	 *
	 * @return Localization key base
	 */
	@Override
	protected String getI18nKeyBase() {
		return "ab.street";
	}

	/**
	 * Create empty name translation
	 *
	 * @return name translation
	 */
	@Override
	public StreetNameTranslation getEmptyNameTranslation() {
		return new StreetNameTranslation();
	}

	/**
	 * Save street districts
	 *
	 * @param street	Street to save districts for
	 * @param objectIds List of district ids
	 * @return saved street object
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	@Override
	public Street saveDistricts(Street street, Set<Long> objectIds) {
		Set<District> districts = new HashSet<District>();
		for (Long id : objectIds) {
			District district = new District();
			district.setId(id);
			districts.add(district);
		}
		street.setDistricts(districts);
		streetDao.update(street);
		return street;
	}

	@Override
	public StreetFilter initFilter(StreetFilter parentFilter, PrimaryKeyFilter forefatherFilter, Locale locale)
			throws FlexPayException {

		if (parentFilter == null) {
			parentFilter = new StreetFilter();
		}

		parentFilter.setNames(getTranslations(forefatherFilter, locale));

		Collection<StreetNameTranslation> names = parentFilter.getNames();
		if (names.isEmpty()) {
			throw new FlexPayException("No street names", "ab.no_streets");
		}
		if (parentFilter.getSelectedId() == null || !isFilterValid(parentFilter)) {
			StreetName firstObject = (StreetName) names.iterator().next().getTranslatable();
			parentFilter.setSelectedId(firstObject.getObject().getId());
		}

		return parentFilter;
	}

	private boolean isFilterValid(StreetFilter filter) {

		for (StreetNameTranslation nameTranslation : filter.getNames()) {
			StreetName name = (StreetName) nameTranslation.getTranslatable();
			if (name.getStub().getId().equals(filter.getSelectedId())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public ArrayStack initFilters(ArrayStack filters, Locale locale) throws FlexPayException {
		if (filters == null) {
			filters = new ArrayStack();
		}

		ObjectFilter filter = filters.isEmpty() ? null : (ObjectFilter) filters.pop();
		filters = parentService.initFilters(filters, locale);
		TownFilter forefatherFilter = (TownFilter) filters.peek();

		if (filter instanceof StreetFilter) {
			StreetFilter parentFilter = (StreetFilter) filter;

			// init filter
			parentFilter = initFilter(parentFilter, forefatherFilter, locale);
			filters.push(parentFilter);
		} else if (filter instanceof StreetNameFilter) {
			StreetNameFilter streetNameFilter = (StreetNameFilter) filter;
			// check if selected street is in a town
			if (streetNameFilter.needFilter()) {
				Street selected = readFull(streetNameFilter.getSelectedStub());
				//noinspection ConstantConditions
				if (selected.getTownStub().equals(forefatherFilter.getSelectedStub())) {
					streetNameFilter.setSearchString(format(streetNameFilter.getSelectedStub(), locale, true));
				} else {
					streetNameFilter.setSearchString("");
					streetNameFilter.unsetSelected();
				}
			}
			filters.push(streetNameFilter);
		}

		return filters;
	}

	@NotNull
	@Override
	public List<Street> findByTownAndName(@NotNull Stub<Town> stub, @NotNull String name) {
		return streetDao.findByTownAndName(stub.getId(), name);
	}

	@Override
	public String format(@NotNull Stub<Street> stub, @NotNull Locale locale, boolean shortMode) throws FlexPayException {
		Street street = streetDao.read(stub.getId());
		if (street == null) {
			throw new FlexPayException("Invalid id", "error.invalid_id");
		}
		return street.format(locale, shortMode);
	}

	/**
	 * Create object
	 *
	 * @param object Object to save
	 * @return Saved object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Street create(@NotNull Street object) throws FlexPayExceptionContainer {

		validate(object);
		object.setId(null);
		streetDao.create(object);

		modificationListener.onCreate(object);

		return object;
	}

	/**
	 * Create object
	 *
	 * @param object Object to save
	 * @return Saved object back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Street update(@NotNull Street object) throws FlexPayExceptionContainer {

		validate(object);

		Street old = readFull(stub(object));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No object found to update " + object));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, object);

		streetDao.update(object);
		return object;
	}

	/**
	 * validate district before save
	 *
	 * @param object District object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull Street object) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		if (object.getParent() == null) {
			ex.addException(new FlexPayException("No town", "error.ab.district.no_town"));
		}

		Collection<StreetNameTemporal> temporals = object.getNameTemporals();
		if (temporals.isEmpty()) {
			ex.addException(new FlexPayException("No names", "error.ab.district.no_names"));
		}

		boolean first = true;
		for (StreetNameTemporal temporal : temporals) {

			// the second and all next names should have default lang translation
			if (!first || temporals.size() == 1) {
				StreetName name = object.getNameForDate(DateUtil.now());
				if (name == null || StringUtils.isBlank(name.getDefaultNameTranslation())) {
					FlexPayException e = new FlexPayException("No translation", "error.no_default_translation",
							temporal.getBegin(), temporal.getEnd());
					ex.addException(e);
					log.debug("Period: {} - {} is empty ", temporal.getBegin(), temporal.getEnd());
					break;
				}
			}

			first = false;
		}

		if (ex.isNotEmpty()) {
			throw ex;
		}
	}

	/**
	 * Disable NTD
	 *
	 * @param objects NTDs to disable
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if failure occurs
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(Collection<Street> objects) throws FlexPayExceptionContainer {

		log.info("{} districts to disable", objects.size());
		for (Street object : objects) {
			object.setStatus(StreetType.STATUS_DISABLED);
			streetDao.update(object);

			modificationListener.onDelete(object);

			log.info("Disabled: {}", object);
		}
	}

	/**
	 * Disable streets
	 *
	 * @param objectIds Streets identifiers
	 */
	@Transactional (readOnly = false)
	@Override
	public void disableByIds(@NotNull Collection<Long> objectIds) {
		for (Long id : objectIds) {
			Street street = streetDao.read(id);
			if (street != null) {
				street.disable();
				streetDao.update(street);

				modificationListener.onDelete(street);
				log.debug("Disabled: {}", street);
			}
		}
	}

	@Override
	public List<Street> find(ArrayStack filters, Page pager) {
		ObjectFilter filter = (ObjectFilter) filters.peek();

		// found street name filter, lookup for town filter and search via name
		if (filter instanceof StreetNameFilter) {
			if (filter.needFilter()) {
				StreetNameFilter nameFilter = (StreetNameFilter) filter;
				Street street = readFull(nameFilter.getSelectedStub());
				log.debug("Streets search unique result");
				return street != null ? CollectionUtils.list(street) : Collections.<Street>emptyList();
			}

			// remove not needed StreetNameFilter
			log.debug("Removing StreetNameFilter");
			filters.pop();
		}

		log.debug("Streets search redirected to super()");

		return super.find(filters, pager);
	}

	@NotNull
	@Override
	public List<Street> find(ArrayStack filters, List<ObjectSorter> sorters, Page<Street> pager) {
		ObjectFilter filter = (ObjectFilter) filters.peek();

		// found street name filter, lookup for town filter and search via name
		if (filter instanceof StreetNameFilter) {
			if (filter.needFilter()) {
				return find(filters, pager);
			}
			// remove name filter as there is nothing to search now
			filters.pop();
		}

		log.debug("Finding town streets with sorters");
		PrimaryKeyFilter<?> townFilter = (PrimaryKeyFilter<?>) filters.peek();
		return streetDaoExt.findStreets(townFilter.getSelectedId(), sorters, pager);
	}

	@NotNull
	@Override
	public List<Street> getStreets(@NotNull Stub<Town> townStub, List<ObjectSorter> sorters, Page<Street> pager) {
		log.debug("Finding streets with sorters");
		Town town = townDao.read(townStub.getId());
		if (town == null) {
			log.warn("No town found for id {}", townStub.getId());
			return Collections.emptyList();
		}

		return streetDaoExt.findStreets(townStub.getId(), sorters, pager);
	}

	@NotNull
	@Override
	public List<Street> findByTownAndQuery(@NotNull Stub<Town> townStub, List<ObjectSorter> sorters, @NotNull String query, Page<Street> pager) {
		log.debug("Finding streets with sorters and query");
		Town town = townDao.read(townStub.getId());
		if (town == null) {
			log.warn("No town found for id {}", townStub.getId());
			return Collections.emptyList();
		}

		return streetDaoExt.findByTownAndQuery(townStub.getId(), sorters, query, pager);
	}

	@NotNull
	@Override
	public List<Street> findByTownAndQuery(@NotNull Stub<Town> stub, @NotNull String query) {
		return streetDao.findByTownAndQuery(stub.getId(), query);
	}

	/**
	 * Read name time-dependent object by its unique id
	 *
	 * @param stub Object stub
	 * @return object, or <code>null</code> if not found
	 */
	@Override
	public Street readFull(@NotNull Stub<Street> stub) {

		Street street = streetDao.readFull(stub.getId());
		if (street == null) {
			return null;
		}

		street.setTypeTemporals(CollectionUtils.treeSet(streetDao.findTypeTemporals(stub.getId())));
		street.getDistricts().addAll(streetDao.findDistricts(stub.getId()));

		return street;
	}

	/**
	 * Read streets
	 *
	 * @param stubs Street keys
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@NotNull
	@Override
	public List<Street> readFull(@NotNull Collection<Long> stubs) {

		List<Street> streets = streetDao.readFullCollection(stubs, true);
		if (log.isDebugEnabled()) {
			log.debug("Requested {} streets, fetched {}", stubs.size(), streets.size());
		}

		return streets;
	}

	/**
	 * List all districts the street lays in
	 *
	 * @param stub Street stub
	 * @return List of districts
	 */
	@NotNull
	public List<District> getStreetDistricts(@NotNull Stub<Street> stub) {
		return streetDao.findDistricts(stub.getId());
	}

	@Override
	@Transactional (readOnly = false)
	public void delete(Street street) {
		streetDaoExt.deleteStreet(street.getId());
	}

	@Required
	public void setStreetDao(StreetDao streetDao) {
		this.streetDao = streetDao;
	}

	@Required
	public void setStreetDaoExt(StreetDaoExt streetDaoExt) {
		this.streetDaoExt = streetDaoExt;
	}

	@Required
	public void setStreetNameDao(StreetNameDao streetNameDao) {
		this.streetNameDao = streetNameDao;
	}

	@Required
	public void setStreetNameTemporalDao(StreetNameTemporalDao streetNameTemporalDao) {
		this.streetNameTemporalDao = streetNameTemporalDao;
	}

	@Required
	public void setTownDao(TownDao townDao) {
		this.townDao = townDao;
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
	public void setModificationListener(ModificationListener<Street> modificationListener) {
		this.modificationListener = modificationListener;
	}

}
