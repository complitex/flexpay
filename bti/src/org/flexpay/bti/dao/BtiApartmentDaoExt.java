package org.flexpay.bti.dao;

import org.flexpay.bti.persistence.apartment.BtiApartment;

public interface BtiApartmentDaoExt {

	BtiApartment readApartmentWithAttributes(Long apartmentId);

}
