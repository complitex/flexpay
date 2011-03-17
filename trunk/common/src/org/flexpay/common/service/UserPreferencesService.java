package org.flexpay.common.service;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.InputStream;
import java.security.cert.Certificate;
import java.util.List;

public interface UserPreferencesService extends UserDetailsService {

	/**
	 * Locates the user based on the username. In the actual implementation, the search may possibly be case insensitive,
	 * or case insensitive depending on how the implementaion instance is configured. In this case, the
	 * <code>UserDetails</code> object that comes back may have a username that is of a different case than what was
	 * actually requested..
	 *
	 * @param username the username presented to the {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}
	 * @return a fully populated user record (never <code>null</code>)
	 * @throws org.springframework.security.core.userdetails.UsernameNotFoundException
	 *          if the user could not be found or the user has no GrantedAuthority
	 * @throws org.springframework.dao.DataAccessException
	 *          if user could not be found for a repository-specific reason
	 */
	@Override
	@NotNull
	UserPreferences loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException;

	/**
	 * Update UserPreferences object
	 *
	 * @param preferences UserPreferences to update
	 * @return Updated preferences back
	 * @throws FlexPayExceptionContainer if preferences validation fails
	 */
	UserPreferences saveFullData(@NotNull UserPreferences preferences) throws FlexPayExceptionContainer;
	
	/**
	 * Update UserPreferences object
	 *
	 * @param preferences UserPreferences to update
	 * @return Updated preferences back
	 * @throws FlexPayExceptionContainer if preferences validation fails
	 */
	UserPreferences saveAdvancedData(@NotNull UserPreferences preferences) throws FlexPayExceptionContainer;

	/**
	 * Update UserPreferences object
	 *
	 * @param preferences UserPreferences to update
	 * @return Updated preferences back
	 * @throws FlexPayExceptionContainer if preferences validation fails
	 */
	UserPreferences saveGeneralData(@NotNull UserPreferences preferences) throws FlexPayExceptionContainer;

	/**
	 * Update password
	 * @param preferences UserPreferences to update
	 * @param password New password
	 * @return Updated preferences back
	 * @throws FlexPayExceptionContainer if preferences validation fails
	 */
	UserPreferences updatePassword(@NotNull UserPreferences preferences, String password) throws FlexPayExceptionContainer;

	/**
	 * Check user password
	 *
	 * @param preferences UserPreferences
	 * @param password User password
	 * @return <code>true</code> if the password is valid,
	 * <code>false</code> otherwise.
	 * @throws FlexPayExceptionContainer if preferences validation fails
	 */
	boolean checkPassword(@NotNull UserPreferences preferences, String password) throws FlexPayExceptionContainer;

	/**
	 * Get locates the users
	 *
	 * @return users
	 * @throws DataAccessException
	 *         if users could not be found for a repository-specific reason
	 */
	List<UserPreferences> listAllUser() throws DataAccessException;

	/**
	 * Update user role
	 * @param preferences UserPreferences to update
	 * @return Updated preferences back
	 * @throws FlexPayExceptionContainer if preferences validation fails
	 */
	UserPreferences updateUserRole(@NotNull UserPreferences preferences) throws FlexPayExceptionContainer;

	/**
	 * In the actual implementation, the search may possibly be case insensitive,
	 * or case insensitive depending on how the implementaion instance is configured.
	 *
	 * @param userName username presented to the {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}
	 * @return <code>true</code> if user found by name, <code>false</code> otherwise
	 */
	boolean isUserExist(@NotNull String userName);

	/**
	 * Create user preferences
	 *
	 * @param preferences UserPreferences to create
	 * @param password User password
	 * @return Created preferences back
	 * @throws FlexPayExceptionContainer if preferences validation fails
	 */
	UserPreferences createNewUser(@NotNull UserPreferences preferences, String password) throws FlexPayExceptionContainer;

	/**
	 * Delete user preferences by user name
	 *
	 * @param userName User name
	 * @return <code>true</code> if user did not delete, <code>false</code> otherwise
	 */
	boolean deleteUser(@NotNull String userName);

	/**
	 * Create new instance user preference
	 *
	 * @return User preferences
	 */
	UserPreferences createInstanceUserPreferences();

	/**
	 * Edit certificate
	 *
	 * @param preferences User preference
	 * @param description Certificate description
	 * @param inputStreamCertificate Input Stream certificate
	 * @param blocked Blocked certificate
	 * @return  Security certificate instance
	 */
	Certificate editCertificate(@NotNull UserPreferences preferences, String description, Boolean blocked, InputStream inputStreamCertificate);

	/**
	 * Get certificate from user preferences storage
	 *
	 * @param preferences User preferences
	 * @return Security certificate
	 */
    @Nullable
	Certificate getCertificate(@NotNull UserPreferences preferences);

	/**
	 * Delete certificate from user preferences storage
	 *
	 * @param preferences  User preferences
	 */
	void deleteCertificate(@NotNull UserPreferences preferences);

	/**
	 * Get granted authority of user
     *
	 * @param preferences User prefernces
     *
	 * @return Granted authorities or <code>null</code> if user not found
	 */
	@Nullable
	List<String> getGrantedAuthorities(@NotNull UserPreferences preferences);

    /**
     * Is granted role of user
     *
     * @param preferences User prefernces
     * @param role user role name
     *
     * @return true if user has this role, false in other one
     */
    boolean isGrantedAuthorities(@NotNull UserPreferences preferences, @NotNull String role);

}
