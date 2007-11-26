package org.flexpay.common.util.config;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Language;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class UserPreferences {
	private String userName;
	private Locale locale;

	/**
	 * Get current user session preferences
	 *
	 * @param request Http request
	 * @return UserPreferences
	 * @throws FlexPayException if application configuration is invalid
	 */
	public static UserPreferences getPreferences(HttpServletRequest request)
			throws FlexPayException {
		UserPreferences prefs = (UserPreferences) WebUtils.getSessionAttribute(
				request, ApplicationConfig.USER_PREFERENCES_SESSION_ATTRIBUTE);

		// Not found in session, create default one
		if (prefs == null) {
			prefs = new UserPreferences();
			Language lang = ApplicationConfig.getInstance().getDefaultLanguage();
			prefs.locale = lang.getLocale();
		}

		return prefs;
	}

	/**
	 * Save user preferences
	 *
	 * @param request Http request
	 * @param preferences User preferences
	 */
	public static void setPreferences(HttpServletRequest request, UserPreferences preferences) {
		WebUtils.setSessionAttribute(request,
				ApplicationConfig.USER_PREFERENCES_SESSION_ATTRIBUTE, preferences);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
