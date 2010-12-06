package org.flexpay.ab.action.filter;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.filter.FilterAjaxAction;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.FilterObject;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class ApartmentFilterAjaxAction extends FilterAjaxAction {

	private ApartmentService apartmentService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayException {

		if (parents == null) {
			log.warn("Parent parameter is null");
			addActionError(getText("common.error.invalid_id"));
			return SUCCESS;
		}

		Long addressId;

		try {
			addressId = Long.parseLong(parents[0]);
		} catch (Exception e) {
			log.warn("Incorrect building address id in filter ({})", parents[0]);
			addActionError(getText("ab.error.building_address.incorrect_address_id"));
			return SUCCESS;
		}
		if (addressId == 0) {
			return SUCCESS;
		}

		List<Apartment> apartments = apartmentService.findByParent(new Stub<BuildingAddress>(addressId));
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

		filterString = "";

		if (filterValueLong == null || filterValueLong <= 0) {
			return;
		}

		try {
			filterString = apartmentService.getApartmentNumber(new Stub<Apartment>(filterValueLong));
		} catch (Exception e) {
			log.warn("Can't get number for apartment with id = {}", filterValueLong);
			addActionError(getText("ab.error.apartment.cant_get_apartment"));
		}
	}

	@Override
	public void saveFilterValue() {

		if (filterValueLong == null || filterValueLong <= 0) {
			log.warn("Incorrect filter value {}", filterValue);
			addActionError(getText("ab.error.apartment.incorrect_apartment_id"));
			return;
		}

		Apartment apartment = apartmentService.readFull(new Stub<Apartment>(filterValueLong));
		if (apartment == null) {
			log.warn("Can't get apartment with id {} from DB", filterValueLong);
			addActionError(getText("ab.error.apartment.cant_get_apartment"));
			return;
		} else if (apartment.isNotActive()) {
			log.warn("Apartment address with id {} is disabled", filterValueLong);
			addActionError(getText("ab.error.apartment.cant_get_apartment"));
			return;
		}

		getUserPreferences().setApartmentFilter(filterValueLong);
	}

    @Override
    public AbUserPreferences getUserPreferences() {
        return (AbUserPreferences) super.getUserPreferences();
    }

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

}
