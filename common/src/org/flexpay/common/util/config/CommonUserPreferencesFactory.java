package org.flexpay.common.util.config;

import org.flexpay.common.util.config.UserPreferences;

public class CommonUserPreferencesFactory implements UserPreferencesFactory {
	/**
	 * Create new UserPreferences instance
	 *
	 * @return module dependent UserPreferences object
	 */
	@Override
	public UserPreferences newInstance() {
		return new UserPreferences();
	}
}
