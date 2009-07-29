package org.flexpay.common.service.imp;

import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.service.UserPreferencesDefaults;

public class UserPreferencesDefaultsCommon implements UserPreferencesDefaults {

	@Override
	public void setDefaults(UserPreferences preferences) {

		if (preferences.getPageSize() == null ||
			CollectionUtils.set(10, 20, 30).contains(preferences.getPageSize())) {
			preferences.setPageSize(20);
		}

		if (preferences.getLocale() == null) {
			preferences.setLocale(ApplicationConfig.getDefaultLocale());
		}
	}
}
