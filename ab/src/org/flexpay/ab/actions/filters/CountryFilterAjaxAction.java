package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.service.CountryService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

/**
 * Search countries by name
 */
public class CountryFilterAjaxAction extends FilterAjaxAction {

	private CountryService countryService;

	@NotNull
	public String doExecute() throws FlexPayException {

		List<Country> countries = countryService.findByQuery("%" + q + "%");
		log.debug("Found countries: {}", countries);

		foundObjects = new ArrayList<FilterObject>();
		for (Country country : countries) {
			FilterObject object = new FilterObject();
			object.setValue(country.getId() + "");
			object.setName(getTranslation(country.getCountryNames()).getName());
			foundObjects.add(object);
		}

		return SUCCESS;
	}

	public void readFilterString() {
		Country country;
		if (filterValueLong == null) {
			filterValue = ApplicationConfig.getDefaultCountryStub().getId() + "";
			country = countryService.readFull(ApplicationConfig.getDefaultCountryStub());
		} else {
			country = countryService.readFull(new Stub<Country>(filterValueLong));
		}
		filterString = getTranslation(country.getCountryNames()).getName();
	}

	public void saveFilterValue() {
		userPreferences.setCountryFilterValue(filterValue);
		userPreferences.setRegionFilterValue("");
		userPreferences.setTownFilterValue("");
		userPreferences.setDistrictFilterValue("");
		userPreferences.setStreetFilterValue("");
		userPreferences.setBuildingFilterValue("");
		userPreferences.setApartmentFilterValue("");
	}

	@Required
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

}
