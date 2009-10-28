package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class ApartmentFilterAjaxAction extends FilterAjaxAction {

	private ApartmentService apartmentService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayException {

		Long addressId;

		try {
			addressId = Long.parseLong(parents[0]);
		} catch (Exception e) {
			log.warn("Incorrect building address id in filter ({})", parents[0]);
			return SUCCESS;
		}
		if (addressId == 0) {
			return SUCCESS;
		}

		List<Apartment> apartments = apartmentService.getApartments(new Stub<BuildingAddress>(addressId));
		if (log.isDebugEnabled()) {
			log.debug("Found apartments: {}", apartments.size());
		}

		for (Apartment apartment : apartments) {
			foundObjects.add(new FilterObject(apartment.getId() + "", apartment.getNumber()));
		}

		return SUCCESS;
	}

	@Override
	public void readFilterString() {
		if (filterValueLong != null && filterValueLong > 0) {
			try {
				filterString = apartmentService.getApartmentNumber(new Stub<Apartment>(filterValueLong));
			} catch (Exception e) {
				log.debug("Can't get number for apartment with id = {}", filterValueLong);
				filterString = "";
			}
		} else {
			filterString = "";
		}
	}

	@Override
	public void saveFilterValue() {
		getUserPreferences().setApartmentFilter(filterValueLong);
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

}
