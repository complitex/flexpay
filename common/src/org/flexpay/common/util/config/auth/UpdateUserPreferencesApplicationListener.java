package org.flexpay.common.util.config.auth;

import org.flexpay.common.actions.security.LogoutEvent;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.util.config.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.ui.session.HttpSessionDestroyedEvent;

public class UpdateUserPreferencesApplicationListener implements ApplicationListener {

	private Logger log = LoggerFactory.getLogger(getClass());
	private UserPreferencesService preferencesService;

	/**
	 * Handle an application event.
	 *
	 * @param appEvent the event to respond to
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent appEvent) {

		if (appEvent instanceof LogoutEvent) {
			log.debug("LogoutEvent");

			LogoutEvent event = (LogoutEvent) appEvent;
			UserPreferences preferences = (UserPreferences) event.getAuthentication().getPrincipal();
			updateUserPreferences(preferences);
		}

		if (appEvent instanceof HttpSessionDestroyedEvent) {
			log.debug("HttpSessionDestroyedEvent");
			HttpSessionDestroyedEvent event = (HttpSessionDestroyedEvent) appEvent;
			SecurityContext context = (SecurityContext) event.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
			if (context == null) {
				log.warn("Session destroyed, but no SecurityContext found");
				return;
			}
			Authentication auth = context.getAuthentication();
			UserPreferences preferences = (UserPreferences) auth.getPrincipal();
			updateUserPreferences(preferences);
		}
	}

	private void updateUserPreferences(UserPreferences preferences) {
		try {
			preferencesService.saveAdvancedData(preferences);
		} catch (FlexPayExceptionContainer ex) {
			ex.error(log, "Failed updating user preferences {}", preferences);
		} catch (Exception ex) {
			log.error("Failed updating user preferences " + preferences, ex);
		}
	}

	@Required
	public void setPreferencesService(UserPreferencesService preferencesService) {
		this.preferencesService = preferencesService;
	}
}
