package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.dao.ApartmentDaoExt;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.filters.*;
import org.flexpay.ab.service.ApartmentService;
import static org.flexpay.ab.util.TranslationUtil.getNameTranslation;
import static org.flexpay.ab.util.TranslationUtil.getTypeTranslation;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class ApartmentServiceImpl implements ApartmentService {

	@NonNls
	private Logger log = Logger.getLogger(getClass());

	private ApartmentDao apartmentDao;
	private ApartmentDaoExt apartmentDaoExt;

	private ParentService<BuildingsFilter> parentService;

	@NotNull
	public String getAddress(@NotNull Stub<Apartment> stub) throws FlexPayException {
		Apartment apartment = apartmentDao.read(stub.getId());
		if (apartment == null) {
			throw new FlexPayException("Invalid apartment id: " + stub.getId());
		}

		Building building = apartment.getBuilding();
		Buildings buildings = building.getDefaultBuildings();
		if (buildings == null) {
			throw new FlexPayException("No building attributes",
					"error.ab.building.no_attributes", apartment.getBuilding().getId());
		}
		Street street = buildings.getStreet();
		String streetNameStr = getNameTranslation(street);
		String streetTypeStr = getTypeTranslation(street);

		return streetTypeStr + " " + streetNameStr + ", д."
			   + buildings.getNumber() + ", кв." + apartment.getNumber();
	}

	/**
	 * Try to find apartment by building and number
	 *
	 * @param building Building
	 * @param number   Apartment number
	 * @return Apartment if found, or <code>null</code> otherwise
	 */
	@Nullable
	public Stub<Apartment> findApartmentStub(@NotNull Building building, String number) {
		return apartmentDaoExt.findApartmentStub(building, number);
	}

	public String getApartmentNumber(Stub<Apartment> apartment) throws FlexPayException {

		Apartment persistent = apartmentDao.readFull(apartment.getId());
		if (persistent == null) {
			throw new FlexPayException("error.invalid_id");
		}
		return persistent.getNumber();
	}

	/**
	 * Disable apartments
	 *
	 * @param objectIds Apartments identifiers
	 */
	public void disable(@NotNull Set<Long> objectIds) {
		for (Long id : objectIds) {
			Apartment apartment = apartmentDao.read(id);
			if (apartment != null) {
				apartment.disable();
				apartmentDao.update(apartment);
			}
		}
	}

	/**
	 * Create or update apartment
	 *
	 * @param apartment Apartment to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Transactional (readOnly = false)
	public void save(@NotNull Apartment apartment) throws FlexPayExceptionContainer {
		validate(apartment);
		if (apartment.isNew()) {
			apartment.setId(null);
			apartmentDao.create(apartment);
		} else {
			apartmentDao.update(apartment);
		}
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(@NotNull Apartment apartment) throws FlexPayExceptionContainer {
		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (apartment.hasNoBuilding()) {
			container.addException(new FlexPayException(
					"No building", "ab.error.apartment.no_building"));
		}

		if (apartment.hasNoNumber()) {
			container.addException(new FlexPayException(
					"No number", "ab.error.apartment.no_number"));
		}

		// check if this number already exists
		if (apartment.hasBuilding() && apartment.hasNumber()) {
			Stub<Apartment> stub = apartmentDaoExt.findApartmentStub(
					apartment.getBuilding(), apartment.getNumber());
			if (stub != null && !stub.getId().equals(apartment.getId())) {
				container.addException(new FlexPayException(
						"Number duplicate", "ab.error.apartment.number_duplicate"));
			}
		}

		if (container.isNotEmpty()) {
			throw container;
		}
	}

	/**
	 * Get building apartment belongs to
	 *
	 * @param apartment Apartment stub
	 * @return Building stub
	 */
	@NotNull
	public Building getBuilding(@NotNull Stub<Apartment> apartment) throws FlexPayException {
		Apartment persistent = apartmentDao.read(apartment.getId());
		if (persistent == null) {
			throw new FlexPayException("Invalid id", "error.invalid_id");
		}
		return persistent.getBuilding();
	}

	/**
	 * Read apartment with registered persons
	 *
	 * @param stub Apartment stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	public Apartment readWithPersons(@NotNull Stub<Apartment> stub) {
		List<Apartment> apartments = apartmentDao.findWithPersonsFull(stub.getId());
		return apartments.isEmpty() ? null : apartments.get(0);
	}

	public void fillFilterIds(@NotNull Stub<Apartment> stub, ArrayStack filters) throws FlexPayException {
		Apartment apartment = apartmentDao.read(stub.getId());
		if (apartment == null) {
			throw new FlexPayException("Invalid apartment id: " + stub.getId());
		}
		for (int n = 0; n < filters.size(); ++n) {
			ObjectFilter filter = (ObjectFilter) filters.peek(n);
			if (filter instanceof ApartmentFilter) {
				ApartmentFilter apartmentFilter = (ApartmentFilter) filter;
				apartmentFilter.setSelectedId(apartment.getId());
			} else if (filter instanceof BuildingsFilter) {
				BuildingsFilter buildingsFilter = (BuildingsFilter) filter;
				buildingsFilter.setSelectedId(apartment.getDefaultBuildings().getId());
			} else if (filter instanceof StreetFilter) {
				StreetFilter streetFilter = (StreetFilter) filter;
				streetFilter.setSelectedId(apartment.getDefaultStreet().getId());
			} else if (filter instanceof DistrictFilter) {
				DistrictFilter districtFilter = (DistrictFilter) filter;
				districtFilter.setSelectedId(apartment.getDistrict().getId());
			} else if (filter instanceof TownFilter) {
				TownFilter townFilter = (TownFilter) filter;
				townFilter.setSelectedId(apartment.getTown().getId());
			} else if (filter instanceof RegionFilter) {
				RegionFilter regionFilter = (RegionFilter) filter;
				regionFilter.setSelectedId(apartment.getRegion().getId());
			} else if (filter instanceof CountryFilter) {
				CountryFilter countryFilter = (CountryFilter) filter;
				countryFilter.setSelectedId(apartment.getCountry().getId());
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Unsupported filter: " + filter);
				}
			}
		}
	}

	/**
	 * Initialize parent filter. Possibly taking in account upper level forefather filter
	 *
	 * @param parentFilter	 Filter to init
	 * @param forefatherFilter Upper level filter
	 * @param locale		   Locale to get parent names in
	 * @return Initialised filter
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public ApartmentFilter initFilter(ApartmentFilter parentFilter, PrimaryKeyFilter forefatherFilter, Locale locale)
			throws FlexPayException {
		if (parentFilter == null) {
			parentFilter = new ApartmentFilter();
		}

		if (log.isInfoEnabled()) {
			log.info("Getting list buildings, forefather filter: " + forefatherFilter);
		}

		ArrayStack filters = new ArrayStack();
		filters.push(forefatherFilter);
		Page pager = new Page(100000, 1);
		parentFilter.setApartments(getApartments(filters, pager));

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

	private boolean isFilterValid(ApartmentFilter filter) {
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

	/**
	 * Initialize filters. <p>Filters are coming from the most significant to less significant ones order, like CountryFilter,
	 * RegionFilter, TownFilter for example</p>
	 *
	 * @param filters Filters to init
	 * @param locale  Locale to get parent names in
	 * @return Initialised filters stack
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public ArrayStack initFilters(ArrayStack filters, Locale locale) throws FlexPayException {
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

	public List<Apartment> getApartments(ArrayStack filters, Page pager) {
		BuildingsFilter filter = (BuildingsFilter) filters.peek();
		List<Apartment> apartments = apartmentDao.findObjects(filter.getSelectedId(), pager);
		Collections.sort(apartments, new Comparator<Apartment>() {
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

	public void setApartmentDao(ApartmentDao apartmentDao) {
		this.apartmentDao = apartmentDao;
	}

	public void setApartmentDaoExt(ApartmentDaoExt apartmentDaoExt) {
		this.apartmentDaoExt = apartmentDaoExt;
	}

	public void setParentService(ParentService<BuildingsFilter> parentService) {
		this.parentService = parentService;
	}
}
