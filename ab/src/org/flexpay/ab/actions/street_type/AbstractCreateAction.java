package org.flexpay.ab.actions.street_type;

import java.util.ArrayList;
import java.util.List;

import org.flexpay.ab.persistence.AbstractTranslation;
import org.flexpay.ab.service.MultilangEntityService;
import org.flexpay.common.actions.interceptor.UserPreferencesAware;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.config.UserPreferences;

import com.opensymphony.xwork2.Preparable;

public abstract class AbstractCreateAction<Entity, Translation extends AbstractTranslation>
		implements UserPreferencesAware, Preparable {
	private UserPreferences userPreferences;
	private String submit;
	private List<Translation> translationList;

	protected abstract Translation createTranslation();

	protected abstract MultilangEntityService<Entity, Translation> getEntityService();

	public void prepare() throws FlexPayException {
		translationList = new ArrayList<Translation>();
		List<Language> languageList = ApplicationConfig.getInstance()
				.getLanguages();
		for (Language lang : languageList) {
			Translation translation = createTranslation();
			translation.setLang(lang);
			translationList.add(translation);
		}
	}

	public String execute() throws Exception {

		if (submit != null) {
			try {
				getEntityService().create(translationList);
			} catch (FlexPayException e) {
				// TODO
			}
			return "afterSubmit";
		}

		return "form";
	}
	
	public String getLangName(Language lang) throws FlexPayException {
		return LanguageUtil.getLanguageName(lang, userPreferences.getLocale())
				.getTranslation();
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public void setSubmit(String submit) {
		this.submit = submit;
	}

	public List<Translation> getTranslationList() {
		return translationList;
	}
}
