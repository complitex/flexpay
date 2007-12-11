package org.flexpay.ab.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.actions.interceptor.UserPreferencesAware;
import org.flexpay.common.persistence.LangNameTranslation;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.config.UserPreferences;

import com.opensymphony.xwork2.ActionSupport;

public class StreetTypeCreateAction implements UserPreferencesAware {

	private UserPreferences userPreferences;
	private StreetTypeService streetTypeService;

	public String execute() throws Exception {
		Collection<StreetTypeTranslation> streetTypeTranslations = initTypeTranslations();

		streetTypeService.create(streetTypeTranslations);
		return ActionSupport.SUCCESS;

		// returnActionSupport.INPUT;
	}

	private List<StreetTypeTranslation> initTypeTranslations() throws Exception {
		List<Language> languageList = ApplicationConfig.getInstance()
				.getLanguages();
		List<StreetTypeTranslation> translations = new ArrayList<StreetTypeTranslation>(
				languageList.size());

		for (Language lang : languageList) {
			StreetTypeTranslation translation = new StreetTypeTranslation();
			translation.setLang(lang);
			LangNameTranslation languageName = LanguageUtil.getLanguageName(
					lang, userPreferences.getLocale());
			translation.setTranslation(languageName);

			translations.add(translation);
		}

		return translations;

	}

	/**
	 * Setter for property 'streetTypeService'.
	 * 
	 * @param streetTypeService
	 *            Value to set for property 'streetTypeService'.
	 */
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}
}
