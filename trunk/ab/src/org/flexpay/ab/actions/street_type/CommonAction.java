package org.flexpay.ab.actions.street_type;

import org.flexpay.common.actions.interceptor.UserPreferencesAware;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.config.UserPreferences;

public class CommonAction implements UserPreferencesAware {
	protected UserPreferences userPreferences;
	private String submit;

	public boolean isSubmitted() {
		return submit != null;
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

}
