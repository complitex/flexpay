package org.flexpay.eirc.actions;

import java.util.List;
import java.util.Set;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetNameTranslation;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;

public class StreetAjaxAction extends CommonAction {

	private StreetService streetService;

	private String streetVar;
	private List<Street> streetList;

	public String execute() throws FlexPayException {
		streetList = getStreetListByVar(streetVar);

		return "success";
	}

	private List<Street> getStreetListByVar(String streetVar)
			throws FlexPayException {
		List<Street> streetList = streetService.findByTownAndName(27L,
				streetVar + "%");

		return streetList;
	}

	public Translation getTranslation(Street street) throws FlexPayException {
		Set<StreetNameTranslation> translationSet = street.getCurrentName()
				.getTranslations();
		Language language = LanguageUtil.getLanguage(userPreferences
				.getLocale());
		Language defaultLang = ApplicationConfig.getInstance()
				.getDefaultLanguage();
		Translation result = null;
		for (StreetNameTranslation translation : translationSet) {
			if (language.equals(translation.getLang())) {
				result = translation;
				break;
			} else if (defaultLang.equals(translation.getLang())) {
				result = translation;
			} else if (result == null) {
				result = translation;
			}
		}

		return result;
	}

	/**
	 * @param streetVar
	 *            the streetVar to set
	 */
	public void setStreetVar(String streetVar) {
		this.streetVar = streetVar;
	}

	/**
	 * @return the streetList
	 */
	public List<Street> getStreetList() {
		return streetList;
	}

	/**
	 * @param streetService
	 *            the streetService to set
	 */
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

}
