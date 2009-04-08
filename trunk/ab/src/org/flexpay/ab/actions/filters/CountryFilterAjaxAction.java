package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.config.UserPreferences;
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
		log.debug("!!!!! 1. filterValueLong = {}", filterValueLong);
		Country country = countryService.readFull(new Stub<Country>(filterValueLong));
		log.debug("!!!!! 2. country = {}", country);
		filterString = getTranslation(country.getCountryNames()).getName();
		log.debug("!!!!! 3. filterString = {}", filterString);
	}

	public void saveFilterValue() {
		UserPreferences prefs = UserPreferences.getPreferences(request);
		prefs.setCountryFilterValue(filterValueLong);
		UserPreferences.setPreferences(request, prefs);
	}

	@Required
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

}
