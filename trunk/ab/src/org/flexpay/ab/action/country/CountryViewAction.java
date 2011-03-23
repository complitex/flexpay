package org.flexpay.ab.action.country;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class CountryViewAction extends FPActionSupport {

	private Country country = new Country();

	private CountryService countryService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (country == null || country.isNew()) {
			log.warn("Incorrect country id");
			addActionError(getText("ab.error.country.incorrect_country_id"));
			return REDIRECT_ERROR;
		}

		Stub<Country> stub = stub(country);
		country = countryService.readFull(stub);

		if (country == null) {
			log.warn("Can't get country with id {} from DB", stub.getId());
			addActionError(getText("ab.error.country.cant_get_country"));
			return REDIRECT_ERROR;
		} else if (country.isNotActive()) {
			log.warn("Country with id {} is disabled", stub.getId());
			addActionError(getText("ab.error.country.cant_get_country"));
			return REDIRECT_ERROR;
		}

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Required
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

}
