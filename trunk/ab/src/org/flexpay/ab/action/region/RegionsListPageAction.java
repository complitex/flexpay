package org.flexpay.ab.action.region;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.service.CountryService;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class RegionsListPageAction extends FPActionSupport {

	private Long countryFilter;

	private CountryService countryService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (countryFilter == null || countryFilter < 0) {
			log.warn("Incorrect filter value {}", countryFilter);
			addActionError(getText("ab.error.country.incorrect_country_id"));
			countryFilter = 0L;
		} else if (countryFilter > 0) {
			Country country = countryService.readFull(new Stub<Country>(countryFilter));
			if (country == null) {
				log.warn("Can't get country with id {} from DB", countryFilter);
				addActionError(getText("ab.error.country.cant_get_country"));
				countryFilter = 0L;
			} else if (country.isNotActive()) {
				log.warn("Country with id {} is disabled", countryFilter);
				addActionError(getText("ab.error.country.cant_get_country"));
				countryFilter = 0L;
			}
		}

		if (hasActionErrors()) {
			return SUCCESS;
		}

		AbUserPreferences up = (AbUserPreferences) getUserPreferences();
		up.setCountryFilter(countryFilter);
		up.setRegionFilter(0L);
		up.setTownFilter(0L);
		up.setDistrictFilter(0L);
		up.setStreetFilter(0L);
		up.setBuildingFilter(0L);
		up.setApartmentFilter(0L);

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setCountryFilter(Long countryFilter) {
		this.countryFilter = countryFilter;
	}

	@Required
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}
}
