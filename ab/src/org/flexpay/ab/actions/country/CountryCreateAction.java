package org.flexpay.ab.actions.country;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.flexpay.ab.persistence.CountryNameTranslation;
import org.flexpay.ab.service.CountryService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.LangNameTranslation;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

//TODO: Reconstruct this class for new structure without HttpServletRequest
public class CountryCreateAction extends FPActionSupport {

	private CountryService countryService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayException {
		List<CountryNameTranslation> countryNames = initCountryNames();

		HttpServletRequest request = ServletActionContext.getRequest();

		// Need to create new Country
		if (isSubmit()) {
            if (!doValidate(countryNames)) {
                request.setAttribute("country_names", countryNames);
                return INPUT;
            }

			countryService.create(countryNames);
			return REDIRECT_SUCCESS;
		}

		request.setAttribute("country_names", countryNames);
		return INPUT;
	}

    private boolean doValidate(List<CountryNameTranslation> countryNames) throws FlexPayException {

		HttpServletRequest request = ServletActionContext.getRequest();

        // validate default language country name
        CountryNameTranslation defaultTranslation = TranslationUtil.getTranslation(countryNames);
        String fullName = defaultTranslation.getName();
        String shortName = defaultTranslation.getShortName();

        if (StringUtils.isEmpty(fullName)) {
            addActionError(getText("ab.error.country.full_name_is_required"));
        }

        if (StringUtils.isEmpty(shortName)) {
            addActionError(getText("ab.error.country.short_name_is_required"));
        }

        if (StringUtils.isEmpty(fullName) || StringUtils.isEmpty(shortName)) {
            return false;
        }

        if (!countryService.isNameAvailable(fullName, ApplicationConfig.getDefaultLanguage())) {
            addActionError(getText("ab.error.country.full_name_is_not_available", new String[] { fullName }));
        }

        if (!countryService.isShortNameAvailable(shortName, ApplicationConfig.getDefaultLanguage())) {
            addActionError(getText("ab.error.country.short_name_is_not_available", new String[] { shortName }));
        }

        if (hasActionErrors()) {
            request.setAttribute("country_names", countryNames);            
        }

        return !hasActionErrors();
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

		HttpServletRequest request = ServletActionContext.getRequest();

		List<Language> langs = ApplicationConfig.getLanguages();
		UserPreferences prefs = getUserPreferences();
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

	@Required
	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

}
