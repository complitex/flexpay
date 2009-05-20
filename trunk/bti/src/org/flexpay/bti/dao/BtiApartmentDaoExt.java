package org.flexpay.bti.dao;

import org.flexpay.ab.persistence.Town;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.common.persistence.Stub;

import java.util.List;

public interface BtiApartmentDaoExt {

	BtiApartment readApartmentWithAttributes(Long apartmentId);

	BtiApartment readApartmentWithAttributesByAddress(Long addressId);

	List<BtiApartment> findByTown(Stub<Town> town);

}
