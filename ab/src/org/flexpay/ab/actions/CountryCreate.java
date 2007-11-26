package org.flexpay.ab.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.flexpay.ab.persistence.CountryName;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.LangNameTranslation;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.config.UserPreferences;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class CountryCreate implements ServletRequestAware {

	private static Logger log = Logger.getLogger(CountryCreate.class);

	private CountryService countryService;
	private HttpServletRequest request;

	public String execute() throws FlexPayException {
		List<CountryName> countryNames = initCountryNames();

		// Need to create new Country
		if (isPost()) {
			try {
				countryService.create(countryNames);
				return ActionSupport.SUCCESS;
			} catch (Exception e) {
				log.info("Failed creating country: ", e);
			}
		}

		request.setAttribute("country_names", countryNames);
		return ActionSupport.INPUT;
	}

	/**
	 * Setup Country name translations
	 *
	 * @return List of CountryName
	 * @throws FlexPayException if failure occurs
	 */
	private List<CountryName> initCountryNames() throws FlexPayException {
		List<Language> langs = ApplicationConfig.getInstance().getLanguages();
		UserPreferences prefs = UserPreferences.getPreferences(request);
		List<CountryName> countryNames = new ArrayList<CountryName>(langs.size());
		for (Language lang : langs) {
			CountryName countryName = new CountryName();
			countryName.setLanguage(lang);
			LangNameTranslation languageName = LanguageUtil.getLanguageName(lang, prefs.getLocale());
			countryName.setTranslation(languageName);

			// Actually got a form, extract data
			if (isPost()) {
				countryName.setName(request.getParameter("name_" + lang.getId()));
				countryName.setShortName(request.getParameter("shortname_" + lang.getId()));
			}

			countryNames.add(countryName);
		}

		return countryNames;
	}

	private boolean isPost() {
		return "post".equalsIgnoreCase(request.getMethod());
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
