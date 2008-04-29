package org.flexpay.ab.service.imp;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.dao.ApartmentDaoExt;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetName;
import org.flexpay.ab.persistence.StreetNameTranslation;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.TranslationUtil;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ApartmentServiceImpl implements ApartmentService {

	private ApartmentDao apartmentDao;
	private ApartmentDaoExt apartmentDaoExt;

	/**
	 * Setter for property 'apartmentDao'.
	 * 
	 * @param apartmentDao
	 *            Value to set for property 'apartmentDao'.
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
		StreetName streetName = street.getCurrentName();
		StreetNameTranslation streetNameTranslation = TranslationUtil
				.getTranslation(streetName.getTranslations());
		StreetType streetType = street.getCurrentType();
		StreetTypeTranslation streetTypeTranslation = TranslationUtil
				.getTranslation(streetType.getTranslations());

		return streetTypeTranslation.getShortName() + " "
				+ streetNameTranslation.getName() + ", д."
				+ buildings.getNumber() + ", кв." + apartment.getNumber();
	}

	/**
	 * Try to find apartment by building and number
	 * 
	 * @param building
	 *            Building
	 * @param number
	 *            Apartment number
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
	 * Get building apartment belongs to
	 * 
	 * @param apartment
	 *            Apartment stub
	 * @return Building stub
	 */
	public Building getBuilding(Apartment apartment) {
		Apartment persistent = apartmentDao.read(apartment.getId());
		return persistent.getBuilding();
	}
}
