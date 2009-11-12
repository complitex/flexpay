package org.flexpay.ab.actions.country;

import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class CountryViewAction extends FPActionSupport {

	private Country country = new Country();

	private CountryService countryService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (country.isNew()) {
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		country = countryService.readFull(stub(country));

		if (country == null) {
			addActionError(getText("common.object_not_selected"));
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
