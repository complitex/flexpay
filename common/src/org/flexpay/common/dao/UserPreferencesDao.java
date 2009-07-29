package org.flexpay.common.dao;

import org.flexpay.common.util.config.UserPreferences;


/**
 * Data Access Object interface for the UserPreferences entity.
 */
public interface UserPreferencesDao {

	void update(UserPreferences preferences);

	UserPreferences findByUserName(String uid);
}