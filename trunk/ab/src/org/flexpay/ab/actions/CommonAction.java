package org.flexpay.ab.actions;

import java.util.Set;

import org.flexpay.common.actions.interceptor.UserPreferencesAware;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.util.LanguageUtil;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.common.util.config.UserPreferences;

/**
 * @deprecated use {@link org.flexpay.common.actions.FPActionSupport} insted
 */
public class CommonAction implements UserPreferencesAware {
	private UserPreferences userPreferences;
	private String submitted;

	public boolean isSubmitted() {
		return submitted != null;
	}

	public String getLangName(Language lang) throws FlexPayException {
		return LanguageUtil.getLanguageName(lang, userPreferences.getLocale())
				.getTranslation();
	}

	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public void setSubmitted(String submitted) {
		this.submitted = submitted;
	}
	
	public Translation getTranslation(Set<? extends Translation> translations) throws FlexPayException
	{
		return TranslationUtil.getTranslation(
				translations, userPreferences.getLocale());
	}

	/**
	 * @return the userPreferences
	 */
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

}
