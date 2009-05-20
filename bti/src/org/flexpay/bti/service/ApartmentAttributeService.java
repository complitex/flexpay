package org.flexpay.bti.service;

import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeBase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ApartmentAttributeService {

	/**
	 * Find attributes of a apartment
	 *
	 * @param stub  Apartment stub
	 * @param pager Page
	 * @return list of apartment attributes
	 */
	List<ApartmentAttributeBase> listAttributes(@NotNull Stub<BtiApartment> stub, Page<ApartmentAttributeBase> pager);

    List<ApartmentAttributeBase> listAttributes(@NotNull Stub<BtiApartment> stub);

}
