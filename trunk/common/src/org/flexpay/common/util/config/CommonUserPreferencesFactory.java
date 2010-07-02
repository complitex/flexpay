package org.flexpay.common.util.config;

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
