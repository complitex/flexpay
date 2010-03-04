package org.flexpay.common.dao;

import org.flexpay.common.util.config.UserPreferences;

import java.util.List;


/**
 * Data Access Object interface for the UserPreferences entity.
 */
public interface UserPreferencesDao {
	void save(UserPreferences preferences);

	void delete(UserPreferences preferences);

	UserPreferences findByUserName(String uid);

	List<UserPreferences> listAllUser();
}
