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
	@Override
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

	@Override
	public void readFilterString() {
		Country country;
		if (filterValueLong == null || filterValueLong == 0) {
			filterValueLong = ApplicationConfig.getDefaultCountryStub().getId();
			filterValue = filterValueLong + "";
		}
		country = countryService.readFull(new Stub<Country>(filterValueLong));
		filterString = getTranslation(country.getCountryNames()).getName();
	}

	@Override
	public void saveFilterValue() {
		getUserPreferences().setCountryFilter(filterValueLong);
		getUserPreferences().setRegionFilter(0L);
		getUserPreferences().setTownFilter(0L);
		getUserPreferences().setDistrictFilter(0L);
		getUserPreferences().setStreetFilter(0L);
		getUserPreferences().setBuildingFilter(0L);
		getUserPreferences().setApartmentFilter(0L);
	}

	@Required
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

}
