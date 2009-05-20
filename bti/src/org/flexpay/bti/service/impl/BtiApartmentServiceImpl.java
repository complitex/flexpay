package org.flexpay.bti.service.impl;

import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.persistence.Town;
import org.flexpay.bti.dao.BtiApartmentDaoExt;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.bti.service.BtiApartmentService;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class BtiApartmentServiceImpl implements BtiApartmentService {

	private ApartmentDao apartmentDao;
	private BtiApartmentDaoExt btiApartmentDaoExt;

	/**
	 * Read bti apartment with associated attributes
	 *
	 * @param stub apartment stub to read
	 * @return Apartment if found, or <code>null</code> otherwise
	 */
	public BtiApartment readWithAttributes(Stub<BtiApartment> stub) {
		return btiApartmentDaoExt.readApartmentWithAttributes(stub.getId());
	}

	/**
	 * Update apartment attributes
	 *
	 * @param apartment Apartment to update
	 * @return apartment back
	 */
	@Transactional (readOnly = false)
	public BtiApartment updateAttributes(@NotNull BtiApartment apartment) {
		apartmentDao.update(apartment);

		return apartment;
	}

	/**
	 * Find all BtiApartment in the town
	 *
	 * @param town town to search
	 * @return BtiApartment list in town
	 */
	public List<BtiApartment> findByTown(Stub<Town> town) {
		return btiApartmentDaoExt.findByTown(town);
	}

	@Required
	public void setBtiApartmentDaoExt(BtiApartmentDaoExt btiApartmentDaoExt) {
		this.btiApartmentDaoExt = btiApartmentDaoExt;
	}

	@Required
	public void setApartmentDao(ApartmentDao apartmentDao) {
		this.apartmentDao = apartmentDao;
	}

}
