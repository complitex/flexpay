package org.flexpay.common.util.config;

public interface UserPreferencesFactory {

	/**
	 * Create new UserPreferences instance
	 *
	 * @return module dependent UserPreferences object
	 */
	UserPreferences newInstance();
}
