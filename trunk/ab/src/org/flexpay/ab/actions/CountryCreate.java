package org.flexpay.ab.actions;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.flexpay.ab.persistence.CountryNameTranslation;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.LangNameTranslation;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class CountryCreate extends FPActionSupport implements ServletRequestAware {

	private CountryService countryService;
	@NonNls
	private HttpServletRequest request;

	@NotNull
	public String doExecute() throws FlexPayException {
		List<CountryNameTranslation> countryNames = initCountryNames();

		// Need to create new Country
		if (isSubmit()) {
			countryService.create(countryNames);
			return SUCCESS;
		}

		request.setAttribute("country_names", countryNames);
		return INPUT;
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
		return INPUT;
	}

	/**
	 * Setup Country name translations
	 *
	 * @return List of CountryName
	 * @throws FlexPayException if failure occurs
	 */
	private List<CountryNameTranslation> initCountryNames() throws FlexPayException {
		List<Language> langs = ApplicationConfig.getLanguages();
		UserPreferences prefs = UserPreferences.getPreferences(request);
		List<CountryNameTranslation> countryNames = new ArrayList<CountryNameTranslation>();
		for (Language lang : langs) {
			CountryNameTranslation countryName = new CountryNameTranslation();
			countryName.setLang(lang);
			LangNameTranslation languageName = LanguageUtil.getLanguageName(lang, prefs.getLocale());
			countryName.setLangTranslation(languageName);

			// Actually got a form, extract data
			if (isSubmit()) {
				countryName.setName(request.getParameter("name_" + lang.getId()));
				countryName.setShortName(request.getParameter("shortname_" + lang.getId()));
			}

			countryNames.add(countryName);
		}

		return countryNames;
	}

	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

	/**
	 * Sets the HTTP request object in implementing classes.
	 *
	 * @param request the HTTP request.
	 */
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
}
