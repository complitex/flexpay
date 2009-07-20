package org.flexpay.common.util.config;

import org.flexpay.common.util.config.UserPreferences;

public interface UserPreferencesFactory {

	/**
	 * Create new UserPreferences instance
	 *
	 * @return module dependent UserPreferences object
	 */
	UserPreferences newInstance();
}
