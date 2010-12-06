package org.flexpay.ab.action.filter;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.service.CountryService;
import org.flexpay.ab.util.config.AbUserPreferences;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultCountryStub;

import org.flexpay.common.actions.filter.FilterAjaxAction;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.set;

import org.flexpay.common.persistence.filter.FilterObject;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Set;

/**
 * Search countries by name
 */
public class CountryFilterAjaxAction extends FilterAjaxAction {

	private CountryService countryService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayException {

		if (q == null) {
			q = "";
		}

		List<Country> countries = countryService.findByQuery("%" + q + "%");
		if (log.isDebugEnabled()) {
			log.debug("Found countries: {}", countries.size());
		}

		Set<Long> countryIds = set();

		for (Country country : countries) {
			countryIds.add(country.getId());
		}

		countries = countryService.readFull(countryIds, true);

		for (Country country : countries) {
			foundObjects.add(new FilterObject(country.getId() + "", getTranslationName(country.getTranslations())));
		}

		return SUCCESS;
	}

	@Override
	public void readFilterString() {

		if (filterValueLong == null || filterValueLong <= 0) {
			filterValueLong = getDefaultCountryStub().getId();
			filterValue = filterValueLong + "";
		}

		Country country = countryService.readFull(new Stub<Country>(filterValueLong));
		if (country == null) {
			log.warn("Can't get country with id {} from DB", filterValue);
			addActionError(getText("ab.error.country.cant_get_country"));
			filterString = "";
		} else {
			filterString = getTranslationName(country.getTranslations());
		}
	}

	@Override
	public void saveFilterValue() {

		if (filterValueLong == null || filterValueLong <= 0) {
			log.warn("Incorrect filter value {}", filterValue);
			addActionError(getText("ab.error.country.incorrect_country_id"));
			return;
		}

		Country country = countryService.readFull(new Stub<Country>(filterValueLong));
		if (country == null) {
			log.warn("Can't get country with id {} from DB", filterValueLong);
			addActionError(getText("ab.error.country.cant_get_country"));
			return;
		} else if (country.isNotActive()) {
			log.warn("Country with id {} is disabled", filterValueLong);
			addActionError(getText("ab.error.country.cant_get_country"));
			return;
		}

		AbUserPreferences up = getUserPreferences();
		up.setCountryFilter(filterValueLong);
		up.setRegionFilter(0L);
		up.setTownFilter(0L);
		up.setDistrictFilter(0L);
		up.setStreetFilter(0L);
		up.setBuildingFilter(0L);
		up.setApartmentFilter(0L);
	}

    @Override
    public AbUserPreferences getUserPreferences() {
        return (AbUserPreferences) super.getUserPreferences();
    }

	@Required
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

}
