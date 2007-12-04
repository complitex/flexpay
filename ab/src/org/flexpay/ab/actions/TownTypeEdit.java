package org.flexpay.ab.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.LangNameTranslation;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.config.UserPreferences;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class TownTypeEdit implements ServletRequestAware {

	private static Logger log = Logger.getLogger(TownTypeCreate.class);

	private HttpServletRequest request;
	private TownTypeService townTypeService;

	public String execute() throws FlexPayException {
		Long id = Long.parseLong(request.getParameter("town_type_id"));
		TownType townType = townTypeService.read(id);
		List<TownTypeTranslation> townTypeTranslations = initTypeTranslations(townType);

		// Need to update Town Type
		if (isPost()) {
			try {
				townTypeService.update(townType, townTypeTranslations);
				return ActionSupport.SUCCESS;
			} catch (Exception e) {
				log.info("Failed editing town type: ", e);
			}
		}

		request.setAttribute("town_names", townTypeTranslations);
		request.setAttribute("town_type_id", id);
		return ActionSupport.INPUT;
	}

	/**
	 * Initialise Town Type name translations list
	 *
	 * @param townType TownType to get translations for
	 * @return List of town type name translations
	 * @throws FlexPayException if failure occurs
	 */
	private List<TownTypeTranslation> initTypeTranslations(TownType townType)
			throws FlexPayException {
		List<Language> langs = ApplicationConfig.getInstance().getLanguages();
		UserPreferences prefs = UserPreferences.getPreferences(request);
		List<TownTypeTranslation> translations = new ArrayList<TownTypeTranslation>(langs.size());

		// Initialize translation for every language
		for (Language lang : langs) {
			TownTypeTranslation translation = getTranslation(townType, lang);
			LangNameTranslation languageName = LanguageUtil.getLanguageName(lang, prefs.getLocale());
			translation.setTranslation(languageName);

			// Actually got a form, extract data
			if (isPost()) {
				translation.setName(request.getParameter("name_" + lang.getId()));
			}

			translations.add(translation);
		}

		return translations;
	}

	/**
	 * Create or get existing town type translation
	 *
	 * @param townType Town type to get name translation for
	 * @param lang	 Translation language
	 * @return TownTypeTranslation
	 */
	private TownTypeTranslation getTranslation(TownType townType, Language lang) {
		// Check if translation to requested lang already exists
		for (TownTypeTranslation translation : townType.getTypeTranslations()) {
			if (translation.getLang().equals(lang)) {
				return translation;
			}
		}

		// Translation not found, create new one
		TownTypeTranslation translation = new TownTypeTranslation();
		translation.setTownType(townType);
		translation.setLang(lang);

		return translation;
	}

	private boolean isPost() {
		return "post".equalsIgnoreCase(request.getMethod());
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
	 * Setter for property 'townTypeService'.
	 *
	 * @param townTypeService Value to set for property 'townTypeService'.
	 */
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}
}