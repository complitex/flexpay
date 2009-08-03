package org.flexpay.bti.service;

import org.flexpay.bti.persistence.apartment.ApartmentAttribute;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ApartmentAttributeService {

	/**
	 * Find current attributes of an apartment
	 *
	 * @param stub  Apartment stub
	 * @param pager Page
	 * @return list of apartment attributes
	 */
	List<ApartmentAttribute> listAttributes(@NotNull Stub<BtiApartment> stub, Page<ApartmentAttribute> pager);

	List<ApartmentAttribute> listAttributes(@NotNull Stub<BtiApartment> stub);
}
