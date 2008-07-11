package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.dao.ApartmentDaoExt;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.*;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.StreetService;
import static org.flexpay.ab.util.TranslationUtil.getNameTranslation;
import static org.flexpay.ab.util.TranslationUtil.getTypeTranslation;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class ApartmentServiceImpl implements ApartmentService {

	private ApartmentDao apartmentDao;
	private ApartmentDaoExt apartmentDaoExt;

	private StreetService streetService;

	/**
	 * Setter for property 'apartmentDao'.
	 *
	 * @param apartmentDao Value to set for property 'apartmentDao'.
	 */
	public void setApartmentDao(ApartmentDao apartmentDao) {
		this.apartmentDao = apartmentDao;
	}

	public void setApartmentDaoExt(ApartmentDaoExt apartmentDaoExt) {
		this.apartmentDaoExt = apartmentDaoExt;
	}

	public List<Apartment> getApartments(ArrayStack filters, Page pager) {
		BuildingsFilter filter = (BuildingsFilter) filters.peek();
		return apartmentDao.findObjects(filter.getSelectedId(), pager);
	}

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
					"error.ab.building.no_attributes", String.valueOf(apartment.getBuilding().getId()));
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

	public String getApartmentNumber(Apartment apartment) throws FlexPayException {

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
	 * @param apartment Apartment
	 * @param number	apartment number
	 * @throws ObjectAlreadyExistException
	 */
	@Transactional (readOnly = false)
	public void setApartmentNumber(Apartment apartment, String number) throws ObjectAlreadyExistException {
		apartment = apartmentDao.read(apartment.getId());
		apartment.setNumber(number);
		apartmentDao.update(apartment);
	}

	/**
	 * Get building apartment belongs to
	 *
	 * @param apartment Apartment stub
	 * @return Building stub
	 */
	public Building getBuilding(Apartment apartment) {
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

	public void fillFilterIds(@NotNull Apartment apartment, CountryFilter countryFilter, RegionFilter regionFilter, TownFilter townFilter, StreetFilter streetFilter, BuildingsFilter buildingsFilter) {
		apartment = apartmentDao.read(apartment.getId());
		Buildings buildings = apartment.getBuilding().getDefaultBuildings();
		buildingsFilter.setSelectedId(buildings.getId());
		Street street = buildings.getStreet();
		streetFilter.setSelectedId(street.getId());
		Town town = (Town) street.getParent();
		townFilter.setSelectedId(town.getId());
		Region region = (Region) town.getParent();
		regionFilter.setSelectedId(region.getId());
		countryFilter.setSelectedId(region.getParent().getId());
	}
}
