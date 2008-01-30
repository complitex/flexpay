package org.flexpay.ab.actions.town;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.config.ApplicationConfig;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public class TownTypeEdit extends FPActionSupport
		implements ServletRequestAware, SessionAware {

	private static Logger log = Logger.getLogger(TownTypeEdit.class);

	private HttpServletRequest request;
	private Map<String, Object> session;
	private TownTypeService townTypeService;

	private Long townTypeId;
	List<TownTypeTranslation> townTypeTranslations = Collections.emptyList();

	public String execute() throws FlexPayException {
		Long id;
		try {
			townTypeId = Long.parseLong(request.getParameter("townTypeId"));
		} catch (NumberFormatException e) {
			// No id specified, redirect successfully
			log.debug("No id specified, redirecting to success view");
			addActionError(getText("error.town_type_no_id"));
			TownTypesList.setActionErrors(session, getActionErrors());
			return SUCCESS;
		}

		TownType townType = townTypeService.read(townTypeId);
		if (townType == null) {
			addActionError(getText("error.town_type_invalid_id"));
			TownTypesList.setActionErrors(session, getActionErrors());
			return SUCCESS;
		}

		townTypeTranslations = initTypeTranslations(townType);

		// Need to update Town Type
		if (isPost()) {
			try {
				townTypeService.update(townType, townTypeTranslations);
				return SUCCESS;
			} catch (FlexPayException e) {
				log.info("Failed editing town type: ", e);
				addActionError(e);
			}
		}

		return INPUT;
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
		List<TownTypeTranslation> translations = new ArrayList<TownTypeTranslation>();

		// Initialize translation for every language
		for (Language lang : langs) {
			TownTypeTranslation translation = getTranslation(townType, lang);

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
		for (TownTypeTranslation translation : townType.getTranslations()) {
			if (translation.getLang().equals(lang)) {
				return translation;
			}
		}

		// Translation not found, create new one
		TownTypeTranslation translation = new TownTypeTranslation();
		translation.setTranslatable(townType);
		translation.setLang(lang);

		return translation;
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

	/**
	 * Getter for property 'townTypeId'.
	 *
	 * @return Value for property 'townTypeId'.
	 */
	public Long getTownTypeId() {
		return townTypeId;
	}

	/**
	 * Getter for property 'townTypeTranslations'.
	 *
	 * @return Value for property 'townTypeTranslations'.
	 */
	public List<TownTypeTranslation> getTownTypeTranslations() {
		return townTypeTranslations;
	}
}