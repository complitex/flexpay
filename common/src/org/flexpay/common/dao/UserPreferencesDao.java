package org.flexpay.common.dao;

import org.flexpay.common.util.config.UserPreferences;

import java.util.List;


/**
 * Data Access Object interface for the UserPreferences entity.
 */
public interface UserPreferencesDao {
	void createNewUser(UserPreferences preferences, String password);

	void saveAllPreferences(UserPreferences preferences);

	void saveUserEditedPreferences(UserPreferences preferences);

	void saveAdminEditedPreferences(UserPreferences preferences);

	void updateUserPassword(UserPreferences preferences, String password);

	boolean checkUserPassword(UserPreferences preferences, String password);

	void changeUserRole(UserPreferences preferences);

	void delete(String uid);

	UserPreferences findByUserName(String uid);

	List<UserPreferences> listAllUser();
}
