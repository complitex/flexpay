package org.flexpay.ab.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.LangNameTranslation;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.config.UserPreferences;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TownTypeEdit extends FPActionSupport
		implements ServletRequestAware, SessionAware {

	private static Logger log = Logger.getLogger(TownTypeEdit.class);

	private HttpServletRequest request;
	private Map<String, Object> session;
	private TownTypeService townTypeService;

	public String execute() throws FlexPayException {
		Long id;
		try {
			id = Long.parseLong(request.getParameter("town_type_id"));
		} catch (NumberFormatException e) {
			// No id specified, redirect successfully
			log.debug("No id specified, redirecting to success view");
			addActionError(getText("error.town_type_no_id"));
			TownTypesList.setActionErrors(session, getActionErrors());
			return ActionSupport.SUCCESS;
		}

		TownType townType = townTypeService.read(id);
		if (townType == null) {
			addActionError(getText("error.town_type_invalid_id"));
			TownTypesList.setActionErrors(session, getActionErrors());
			return ActionSupport.SUCCESS;
		}

		List<TownTypeTranslation> townTypeTranslations = initTypeTranslations(townType);

		// Need to update Town Type
		if (isPost()) {
			try {
				townTypeService.update(townType, townTypeTranslations);
				return ActionSupport.SUCCESS;
			} catch (FlexPayException e) {
				log.info("Failed editing town type: ", e);
				addActionError(e);
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
		List<TownTypeTranslation> translations = new ArrayList<TownTypeTranslation>();

		// Initialize translation for every language
		for (Language lang : langs) {
			TownTypeTranslation translation = getTranslation(townType, lang);
			LangNameTranslation languageName = LanguageUtil.getLanguageName(
					lang, prefs.getLocale());
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
	 * Sets the Map of session attributes in the implementing class.
	 *
	 * @param session a Map of HTTP session attribute name/value pairs.
	 */
	@SuppressWarnings ({"unchecked"})
	public void setSession(Map session) {
		this.session = session;
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