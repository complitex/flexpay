package org.flexpay.common.dao;

import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.security.cert.Certificate;
import java.util.List;


/**
 * Data Access Object interface for the UserPreferences entity.
 */
public interface UserPreferencesDao {
	boolean createNewUser(UserPreferences preferences, String password);

	void saveAllPreferences(UserPreferences preferences);

	void saveUserEditedPreferences(UserPreferences preferences);

	void saveAdminEditedPreferences(UserPreferences preferences);

	void updateUserPassword(UserPreferences preferences, String password);

	boolean checkUserPassword(UserPreferences preferences, String password);

	void changeUserRole(UserPreferences preferences);

	boolean delete(String uid);

	UserPreferences findByUserName(String uid);

	List<UserPreferences> listAllUser();

	Certificate editCertificate(UserPreferences preferences, String description, Boolean blocked, InputStream inputStreamCertificate);

    @Nullable
	Certificate getCertificate(UserPreferences preferences);

	void deleteCertificate(UserPreferences preferences);

	boolean isCertificateExist(UserPreferences preferences);
}
