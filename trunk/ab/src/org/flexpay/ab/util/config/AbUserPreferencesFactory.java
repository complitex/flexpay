package org.flexpay.ab.util.config;

import org.flexpay.common.util.config.UserPreferencesFactory;

public class AbUserPreferencesFactory implements UserPreferencesFactory {

	/**
	 * Create new UserPreferences instance
	 *
	 * @return module dependent UserPreferences object
	 */
	@Override
	public AbUserPreferences newInstance() {
		return new AbUserPreferences();
	}
}
