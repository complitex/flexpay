package org.flexpay.ab.actions.country;

import org.flexpay.ab.persistence.CountryNameTranslation;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.actions.FPActionSupport;
import org.springframework.beans.factory.annotation.Required;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CountriesListAction extends FPActionSupport {

	private List<CountryNameTranslation> translationList;

	private CountryService countryService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		translationList = countryService.getCountries(getUserPreferences().getLocale());

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
		return SUCCESS;
	}

	public List<CountryNameTranslation> getTranslationList() {
		return translationList;
	}

	@Required
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

}
