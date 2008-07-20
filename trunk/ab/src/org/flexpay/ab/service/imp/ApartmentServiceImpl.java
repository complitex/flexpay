package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.dao.ApartmentDaoExt;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.*;
import org.flexpay.ab.service.ApartmentService;
import static org.flexpay.ab.util.TranslationUtil.getNameTranslation;
import static org.flexpay.ab.util.TranslationUtil.getTypeTranslation;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
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

//		Pair<Street, String> nameStreetpair = streetService.getFullStreetName(stub(street));

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
	public Apartment findApartmentStub(Building building, String number) {
		return apartmentDaoExt.findApartmentStub(building, number);
	}

	public String getApartmentNumber(Stub<Apartment> apartment) throws FlexPayException {

		Apartment persistent = apartmentDao.readFull(apartment.getId());
		return persistent.getNumber();
	}

	/**
	 * Read full apartment information
	 *
	 * @param id Apartment id
	 * @return Apartment instance, or <code>null</code> if not found
	 */
	public Apartment readFull(Long id) {
		return apartmentDao.readFull(id);
	}

	/**
	 * Validate that given number not alredy exist in given apartment's building. If not exist then set new number for given
	 * apartment.
	 *
	 * @param stub   Apartment stub
	 * @param number apartment number
	 * @throws ObjectAlreadyExistException
	 */
	@Transactional (readOnly = false)
	public void setApartmentNumber(@NotNull Stub<Apartment> stub, String number) throws ObjectAlreadyExistException {
		Apartment apartment = apartmentDao.read(stub.getId());
		apartment.setNumber(number);
		apartmentDao.update(apartment);
	}

	/**
	 * Get building apartment belongs to
	 *
	 * @param apartment Apartment stub
	 * @return Building stub
	 */
	public Building getBuilding(@NotNull Stub<Apartment> apartment) {
		Apartment persistent = apartmentDao.read(apartment.getId());
		return persistent.getBuilding();
	}

	@Transactional (readOnly = false, rollbackFor = Exception.class)
	public void create(Apartment apartment) {
		apartmentDao.create(apartment);
	}

	/**
	 * Read apartment with registered persons
	 *
	 * @param id Object identifier
	 * @return Object if found, or <code>null</code> otherwise
	 */
	public Apartment readWithPersons(Long id) {
		List<Apartment> apartments = apartmentDao.findWithPersonsFull(id);
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
					log.info("Unsupported filter: " + filter);
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

		ApartmentFilter parentFilter = filters.isEmpty() ? null
									   : (ApartmentFilter) filters.pop();

		filters = parentService.initFilters(filters, locale);
		BuildingsFilter forefatherFilter = (BuildingsFilter) filters.peek();

		// init filter
		parentFilter = initFilter(parentFilter, forefatherFilter, locale);
		filters.push(parentFilter);

		return filters;

	}

	public List<Apartment> getApartments(ArrayStack filters, Page pager) {
		BuildingsFilter filter = (BuildingsFilter) filters.peek();
		List<Apartment> apartments = apartmentDao.findObjects(filter.getSelectedId(), pager);
		Collections.sort(apartments, new Comparator<Apartment>() {
			public int compare(Apartment a1, Apartment a2) {
				return a1.getNumber().compareTo(a2.getNumber());
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
