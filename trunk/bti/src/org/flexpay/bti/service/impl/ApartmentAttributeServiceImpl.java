package org.flexpay.bti.service.impl;

import org.flexpay.bti.dao.BtiApartmentDaoExt;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeBase;
import org.flexpay.bti.service.ApartmentAttributeService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class ApartmentAttributeServiceImpl implements ApartmentAttributeService {

	private BtiApartmentDaoExt apartmentDaoExt;

	/**
	 * Find attributes of a apartment
	 *
	 * @param stub  Apartment stub
	 * @param pager Page
	 * @return list of apartment attributes
	 */
	public List<ApartmentAttributeBase> listAttributes(@NotNull Stub<BtiApartment> stub, Page<ApartmentAttributeBase> pager) {
		List<ApartmentAttributeBase> attributes = listAttributes(stub);
		pager.setTotalElements(attributes.size());
		return CollectionUtils.listSlice(attributes,
				pager.getThisPageFirstElementNumber(), pager.getThisPageLastElementNumber());
	}

	public List<ApartmentAttributeBase> listAttributes(@NotNull Stub<BtiApartment> stub) {
		BtiApartment apartment = apartmentDaoExt.readApartmentWithAttributes(stub.getId());
		return CollectionUtils.list(apartment.getAttributes());
	}

	@Required
	public void setApartmentDaoExt(BtiApartmentDaoExt apartmentDaoExt) {
		this.apartmentDaoExt = apartmentDaoExt;
	}

}
