package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class ApartmentFilterAjaxAction extends FilterAjaxAction {

	private ApartmentService apartmentService;

	@NotNull
	public String doExecute() throws FlexPayException {

		Long addressIdLong;

		try {
			addressIdLong = Long.parseLong(parents[0]);
		} catch (Exception e) {
			log.warn("Incorrect building address id in filter ({})", parents[0]);
			return SUCCESS;
		}

		List<Apartment> apartments = apartmentService.getApartments(new Stub<BuildingAddress>(addressIdLong));
		log.debug("Found apartments: {}", apartments);

		foundObjects = new ArrayList<FilterObject>();
		for (Apartment apartment : apartments) {
			FilterObject object = new FilterObject();
			object.setValue(apartment.getId() + "");
			object.setName(apartment.getNumber());
			foundObjects.add(object);
		}

		return SUCCESS;
	}

	public void readFilterString() {
		if (filterValueLong != null) {
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

	public void saveFilterValue() {
		UserPreferences prefs = UserPreferences.getPreferences(request);
		prefs.setApartmentFilterValue(filterValue);
		UserPreferences.setPreferences(request, prefs);
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

}
