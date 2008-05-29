package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.dao.ApartmentDaoExt;
import org.flexpay.ab.dao.ApartmentNumberDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.TranslationUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ApartmentServiceImpl implements ApartmentService {

	private ApartmentDao apartmentDao;
	private ApartmentDaoExt apartmentDaoExt;
	private ApartmentNumberDao apartmentNumberDao;

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

	public String getAddress(Apartment apartment) throws FlexPayException {
		apartment = apartmentDao.read(apartment.getId());
		Set<Buildings> buildingsSet = apartment.getBuilding().getBuildingses();
		if (buildingsSet.isEmpty()) {
			return "";
		}
		Buildings buildings = buildingsSet.iterator().next();
		Street street = buildings.getStreet();
		String streetNameStr = "";
		if (street.getCurrentName() != null) {
			StreetNameTranslation streetNameTranslation = TranslationUtil
					.getTranslation(street.getCurrentName().getTranslations());
			if (streetNameTranslation != null
					&& streetNameTranslation.getName() != null) {
				streetNameStr = streetNameTranslation.getName();
			}
		}
		String streetTypeStr = "";
		if (street.getCurrentType() != null) {
			StreetTypeTranslation streetTypeTranslation = TranslationUtil
					.getTranslation(street.getCurrentType().getTranslations());
			if (streetTypeTranslation != null) {
				if (streetTypeTranslation.getShortName() != null) {
					streetTypeStr = streetTypeTranslation.getShortName();
				} else if (streetTypeTranslation.getName() != null) {
					streetTypeStr = streetTypeTranslation.getName();
				}
			}
		}

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

	public String getApartmentNumber(Apartment apartment)
			throws FlexPayException {
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
	 * Validate that given number not alredy exist in given apartment's
	 * building. If not exist then set new number for given apartment.
	 *
	 * @param apartment Apartment
	 * @param number	apartment number
	 * @throws ObjectAlreadyExistException
	 */
	@Transactional(readOnly = false)
	public void setApartmentNumber(Apartment apartment, String number) throws ObjectAlreadyExistException {
		apartment = apartmentDao.read(apartment.getId());
		apartment.setNumber(number);
		apartmentDao.update(apartment);

		/*ApartmentNumber apartmentNumber = new ApartmentNumber();
		apartmentNumber.setApartment(apartment);
		apartmentNumber.setBegin(DateIntervalUtil.now());
		apartmentNumber.setEnd(ApplicationConfig.getInstance()
				.getFutureInfinite());
		apartmentNumber.setValue(number);
		apartmentNumberDao.create(apartmentNumber);
		
		if (apartment.getApartmentNumbers().isEmpty()) {
			Set<ApartmentNumber> numberSet = new HashSet<ApartmentNumber>();
			apartment.setApartmentNumbers(numberSet);
		}
		apartment.getApartmentNumbers().add(apartmentNumber);*/
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

	@Transactional(readOnly = false, rollbackFor = Exception.class)
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

	/**
	 * @param apartmentNumberDao the apartmentNumberDao to set
	 */
	public void setApartmentNumberDao(ApartmentNumberDao apartmentNumberDao) {
		this.apartmentNumberDao = apartmentNumberDao;
	}
	
	public void fillFilterIds(Apartment apartment, CountryFilter countryFilter, RegionFilter regionFilter, TownFilter townFilter, StreetFilter streetFilter, BuildingsFilter buildingsFilter) {
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
