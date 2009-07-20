package org.flexpay.common.util.config.auth;

import org.springframework.context.ApplicationListener;
import org.springframework.context.ApplicationEvent;
import org.springframework.beans.factory.annotation.Required;
import org.flexpay.common.actions.security.LogoutEvent;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			LogoutEvent event = (LogoutEvent) appEvent;
			UserPreferences preferences = (UserPreferences) event.getAuthentication().getPrincipal();
			try {
				preferencesService.update(preferences);
			} catch (FlexPayExceptionContainer ex) {
				ex.error(log, "Failed updating user preferences {}", preferences);
			} catch (Exception ex) {
				log.error("Failed updating user preferences " + preferences, ex);
			}
		}
	}

	@Required
	public void setPreferencesService(UserPreferencesService preferencesService) {
		this.preferencesService = preferencesService;
	}
}
