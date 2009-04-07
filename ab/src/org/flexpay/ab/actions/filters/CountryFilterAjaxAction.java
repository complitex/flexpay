package org.flexpay.ab.actions.filters;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.config.UserPreferences;
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

		if (saveFilterValue()) {
			return SUCCESS;
		}

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

	public boolean saveFilterValue() {
		if (filterValue != null) {
			try {
				UserPreferences prefs = UserPreferences.getPreferences(request);
				prefs.setCountryFilterValue(Long.parseLong(filterValue));
				UserPreferences.setPreferences(request, prefs);
				return true;
			} catch (Exception e) {
				log.warn("Incorrect country id in filter ({})", filterValue);
			}
		}
		return false;
	}

	@Required
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

}
