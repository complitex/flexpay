package org.flexpay.ab.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.dao.*;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.ApartmentFilter;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.internal.SessionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.flexpay.common.persistence.Stub.stub;

@Transactional (readOnly = true)
public class ApartmentServiceImpl implements ApartmentService, ParentService<ApartmentFilter> {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ApartmentDao apartmentDao;
	private ApartmentDaoExt apartmentDaoExt;

	private BuildingService buildingService;
	private ParentService<BuildingsFilter> parentService;
	private SessionUtils sessionUtils;
	private ModificationListener<Apartment> modificationListener;

	/**
	 * Read apartment
	 *
	 * @param apartmentStub Apartment stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	public Apartment readFull(@NotNull Stub<Apartment> apartmentStub) {
		return apartmentDao.readFull(apartmentStub.getId());
	}

    /**
     * Read apartment with full hirarchy and names
     *
     * @param apartmentStub Apartment stub
     * @return Object if found, or <code>null</code> otherwise
     */
    @Override
    public Apartment readFullWithHierarchy(@NotNull Stub<Apartment> apartmentStub) {
        List<Apartment> apartments = apartmentDao.findWithFullHierarchyAndNames(apartmentStub.getId());
        if (apartments.isEmpty()) {
            return null;
        }
        return apartments.get(0);
    }

    /**
	 * Read apartments collection by theirs ids
	 *
 	 * @param apartmentIds Apartment ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found regions
	 */
	@NotNull
	@Override
	public List<Apartment> readFull(@NotNull Collection<Long> apartmentIds, boolean preserveOrder) {
		return apartmentDao.readFullCollection(apartmentIds, preserveOrder);
	}

	/**
	 * Disable apartments
	 *
	 * @param apartmentIds IDs of apartments to disable
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(@NotNull Collection<Long> apartmentIds) {
		for (Long id : apartmentIds) {
			if (id == null) {
				log.warn("Null id in collection of apartment ids for disable");
				continue;
			}
			Apartment apartment = apartmentDao.read(id);
			if (apartment == null) {
				log.warn("Can't get apartment with id {} from DB", id);
				continue;
			}
			apartment.disable();
			apartmentDao.update(apartment);

			modificationListener.onDelete(apartment);
			log.debug("Apartment disabled: {}", apartment);
		}
	}

	/**
	 * Create apartment
	 *
	 * @param apartment Apartment to save
	 * @return Saved instance of apartment
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Apartment create(@NotNull Apartment apartment) throws FlexPayExceptionContainer {

		validate(apartment);
		apartment.setId(null);
		apartmentDao.create(apartment);

		modificationListener.onCreate(apartment);

		return apartment;
	}

	/**
	 * Update or create apartment
	 *
	 * @param apartment Apartment to save
	 * @return Saved instance of apartment
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Apartment update(@NotNull Apartment apartment) throws FlexPayExceptionContainer {

		validate(apartment);

		Apartment old = readFull(stub(apartment));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No apartment found to update " + apartment));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, apartment);

		apartmentDao.update(apartment);

		return apartment;
	}

	/**
	 * Validate apartment before save
	 *
	 * @param apartment Apartment object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull Apartment apartment) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (apartment.hasNoBuilding()) {
			container.addException(new FlexPayException("No building", "ab.error.apartment.no_building"));
		}

		if (apartment.hasNoNumber()) {
			container.addException(new FlexPayException("No number", "ab.error.apartment.no_number"));
		}

		// check if this number already exists
		if (apartment.hasBuilding() && apartment.hasNumber()) {
			Stub<Apartment> stub = apartmentDaoExt.findApartmentStub(apartment.getBuilding(), apartment.getNumber());
			if (stub != null && !stub.getId().equals(apartment.getId())) {
				container.addException(new FlexPayException("Number duplicate", "ab.error.apartment.number_duplicate"));
			}
		}

		if (container.isNotEmpty()) {
			throw container;
		}

	}

	/**
	 * Read apartment with registered persons
	 *
	 * @param apartmentStub Apartment stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@NotNull
	@Override
	public Apartment readWithPersons(@NotNull Stub<Apartment> apartmentStub) {
		List<Apartment> apartments = apartmentDao.findWithPersonsFull(apartmentStub.getId());
		return apartments.isEmpty() ? Apartment.newInstance() : apartments.get(0);
	}

	/**
	 * Read apartment with its full hierarchical structure:
	 * country-region-street-building
	 *
	 * @param apartmentStub Apartment stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	public Apartment readWithHierarchy(@NotNull Stub<Apartment> apartmentStub) {
		List<Apartment> apartments = apartmentDao.findWithFullHierarchy(apartmentStub.getId());
		return apartments.isEmpty() ? null : apartments.get(0);
	}

	/**
	 * Try to find apartment by building and number
	 *
	 * @param building Building
	 * @param number   Apartment number
	 * @return Apartment if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	public Stub<Apartment> findApartmentStub(@NotNull Building building, @NotNull String number) {
		return apartmentDaoExt.findApartmentStub(building, number);
	}

	/**
	 * Get a list of available apartments
	 *
	 * @param filters Parent filters
	 * @param sorters Stack of sorters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@NotNull
	@Override
	public List<Apartment> find(@NotNull ArrayStack filters, @NotNull List<? extends ObjectSorter> sorters, @NotNull Page<Apartment> pager) {

		ObjectFilter filter = (ObjectFilter) filters.peek();

		// found apartment filter
		if (filter instanceof ApartmentFilter) {
			if (filter.needFilter()) {
				return find(filters, pager);
			}
			// remove apartment filter as there is nothing to search now
			filters.pop();
		}

		log.debug("Finding building apartments with sorters");
		BuildingsFilter buildingFilter = (BuildingsFilter) filters.peek();
		if (!buildingFilter.needFilter()) {
			return Collections.emptyList();
		}
		Building building = buildingService.findBuilding(buildingFilter.getSelectedStub());
		if (building == null) {
			log.info("No building found for filter {}", buildingFilter);
			return Collections.emptyList();
		}
		return apartmentDaoExt.findApartments(building.getId(), sorters, pager);
	}

	/**
	 * Get a list of available apartments
	 *
	 * @param filters Parent filters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@NotNull
	@Override
	public List<Apartment> find(@NotNull ArrayStack filters, Page<Apartment> pager) {
		BuildingsFilter filter = (BuildingsFilter) filters.peek();
		List<Apartment> apartments = apartmentDao.findObjects(filter.getSelectedId(), pager);
		Collections.sort(apartments, new Comparator<Apartment>() {
			@Override
			public int compare(Apartment a1, Apartment a2) {
				String n1 = a1.getNumber();
				String n2 = a2.getNumber();
				if (n1 == null && n2 == null) {
					return 0;
				}
				if (n1 == null) {
					return -1;
				}
				if (n2 == null) {
					return 1;
				}
				return n1.compareTo(n2);
			}
		});

		return apartments;
	}

	/**
	 * Lookup apartments by building address id.
	 *
	 * @param addressStub  Building address stub
	 * @return List of found apartments
	 */
	@NotNull
	@Override
	public List<Apartment> findByParent(@NotNull Stub<BuildingAddress> addressStub) {
		return apartmentDao.findObjects(addressStub.getId());
	}

	/**
	 * Get apartment number
	 *
	 * @param apartmentStub Apartment stub
	 * @return Apartment number
	 * @throws FlexPayException if apartment specified is invalid
	 */

	@Nullable
	@Override
	public String getApartmentNumber(@NotNull Stub<Apartment> apartmentStub) throws FlexPayException {
		Apartment apartment = apartmentDao.readFull(apartmentStub.getId());
		if (apartment == null) {
			throw new FlexPayException("Invalid id", "common.error.invalid_id");
		}
		return apartment.getNumber();
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
	public ApartmentFilter initFilter(@Nullable ApartmentFilter parentFilter, @NotNull PrimaryKeyFilter<?> forefatherFilter, @NotNull Locale locale)
			throws FlexPayException {

		if (parentFilter == null) {
			parentFilter = new ApartmentFilter();
		}

		ArrayStack filters = new ArrayStack();
		filters.push(forefatherFilter);
		parentFilter.setApartments(find(filters, new Page<Apartment>(100000, 1)));

		List<Apartment> apartments = parentFilter.getApartments();
		if (apartments.isEmpty()) {
			throw new FlexPayException("No apartments", "ab.no_apartments");
		}
		if (parentFilter.getSelectedId() == null || !isFilterValid(parentFilter)) {
			Apartment firstObject = apartments.iterator().next();
			parentFilter.setSelectedId(firstObject.getId());
		}

		return parentFilter;
	}

	private boolean isFilterValid(@NotNull ApartmentFilter filter) {
		if (!filter.needFilter()) {
			return true;
		}
		for (Apartment apartment : filter.getApartments()) {
			if (filter.getSelectedStub().getId().equals(apartment.getId())) {
				return true;
			}
		}

		return false;
	}

	@NotNull
	@Override
	public List<Apartment> findSimpleByTown(Stub<Town> townStub, FetchRange range) {
		return apartmentDao.findSimpleByTown(townStub.getId(), range);
	}

	/**
	 * Initialize filters. <p>Filters are coming from the most significant to less significant ones order, like
	 * CountryFilter, RegionFilter, TownFilter for example</p>
	 *
	 * @param filters Filters to init
	 * @param locale  Locale to get parent names in
	 * @return Initialised filters stack
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	@Override
	public ArrayStack initFilters(@Nullable ArrayStack filters, @NotNull Locale locale) throws FlexPayException {
		if (filters == null) {
			filters = new ArrayStack();
		}

		ArrayStack stack = new ArrayStack();
		while (!filters.isEmpty() && !(filters.peek() instanceof ApartmentFilter)) {
			stack.push(filters.pop());
		}

		ApartmentFilter parentFilter = filters.isEmpty() ? null
														 : (ApartmentFilter) filters.pop();

		filters = parentService.initFilters(filters, locale);
		BuildingsFilter forefatherFilter = (BuildingsFilter) filters.peek();

		// init filter
		parentFilter = initFilter(parentFilter, forefatherFilter, locale);
		filters.push(parentFilter);

		while (!stack.isEmpty()) {
			filters.push(stack.pop());
		}

		return filters;
	}

	@Required
	public void setApartmentDao(ApartmentDao apartmentDao) {
		this.apartmentDao = apartmentDao;
	}

    @Required
	public void setApartmentDaoExt(ApartmentDaoExt apartmentDaoExt) {
		this.apartmentDaoExt = apartmentDaoExt;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<Apartment> modificationListener) {
		this.modificationListener = modificationListener;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setParentService(ParentService<BuildingsFilter> parentService) {
		this.parentService = parentService;
	}

}
