package org.flexpay.bti.service.impl;

import org.flexpay.bti.dao.BtiApartmentDaoExt;
import org.flexpay.bti.persistence.apartment.ApartmentAttribute;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.bti.service.ApartmentAttributeService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
	public List<ApartmentAttribute> listAttributes(@NotNull Stub<BtiApartment> stub, Page<ApartmentAttribute> pager) {
		List<ApartmentAttribute> attributes = listAttributes(stub);
		pager.setTotalElements(attributes.size());
		return CollectionUtils.listSlice(attributes,
				pager.getThisPageFirstElementNumber(), pager.getThisPageLastElementNumber());
	}

	public List<ApartmentAttribute> listAttributes(@NotNull Stub<BtiApartment> stub) {
		BtiApartment apartment = apartmentDaoExt.readApartmentWithAttributes(stub.getId());
		if (apartment == null) {
			return Collections.emptyList();
		}
		return CollectionUtils.list(apartment.currentAttributes());
	}

	@Required
	public void setApartmentDaoExt(BtiApartmentDaoExt apartmentDaoExt) {
		this.apartmentDaoExt = apartmentDaoExt;
	}
}
