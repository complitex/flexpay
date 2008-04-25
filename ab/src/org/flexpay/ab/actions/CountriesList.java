package org.flexpay.ab.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.flexpay.ab.persistence.CountryNameTranslation;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.config.UserPreferences;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class CountriesList implements ServletRequestAware {

	private CountryService countryService;
	private HttpServletRequest request;
	private List<CountryNameTranslation> translationList;

	public String execute() throws FlexPayException {
		UserPreferences prefs = UserPreferences.getPreferences(request);
		translationList = countryService.getCountries(prefs.getLocale());
		//request.setAttribute("country_names", countryNames);

		return ActionSupport.SUCCESS;
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

	/**
	 * @return the translationList
	 */
	public List<CountryNameTranslation> getTranslationList() {
		return translationList;
	}
}
