package org.flexpay.common.service;

import org.flexpay.common.util.config.UserPreferences;

/**
 * Instances of this interface setup default user preferences attributes if needed
 */
public interface UserPreferencesDefaults {

	/**
	 * Check if default values needed to be set and set them
	 * 
	 * @param preferences UserPreferences to set defaults for
	 */
	void setDefaults(UserPreferences preferences);
}
