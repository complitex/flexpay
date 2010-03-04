package org.flexpay.common.service;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserPreferencesService extends UserDetailsService {

	/**
	 * Locates the user based on the username. In the actual implementation, the search may possibly be case insensitive,
	 * or case insensitive depending on how the implementaion instance is configured. In this case, the
	 * <code>UserDetails</code> object that comes back may have a username that is of a different case than what was
	 * actually requested..
	 *
	 * @param username the username presented to the {@link org.springframework.security.providers.dao.DaoAuthenticationProvider}
	 * @return a fully populated user record (never <code>null</code>)
	 * @throws org.springframework.security.userdetails.UsernameNotFoundException
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
	UserPreferences save(@NotNull UserPreferences preferences) throws FlexPayExceptionContainer;

	/**
	 * Get locates the users
	 *
	 * @return users
	 * @throws DataAccessException
	 *         if users could not be found for a repository-specific reason
	 */
	List<UserPreferences> listAllUser() throws DataAccessException;
}
