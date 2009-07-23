package org.flexpay.common.service.imp;

import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.util.config.UserPreferences;
import org.springframework.security.Authentication;
import org.springframework.security.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Required;

public class UserPreferencesAuthenticationService implements AuthenticationUserDetailsService {

	private UserPreferencesService userPreferencesService;
	private AuthenticationUserDetailsService detailsService = null;

	/**
	 * @param token The pre-authenticated authentication token
	 * @return UserDetails for the given authentication token, never null.
	 * @throws org.springframework.security.userdetails.UsernameNotFoundException
	 *          if no user details can be found for the given authentication token
	 */
	@Override
	public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
		UserDetails userDetails = token.getPrincipal() instanceof UserDetails ?
								  (UserDetails) token.getPrincipal() :
								  detailsService.loadUserDetails(token);
		UserPreferences userPreferences = userPreferencesService.loadUserByUsername(token.getName());
		userPreferences.setTargetDetails(userDetails);
		return userPreferences;
	}

	@Required
	public void setUserPreferencesService(UserPreferencesService userPreferencesService) {
		this.userPreferencesService = userPreferencesService;
	}

	public void setDetailsService(AuthenticationUserDetailsService detailsService) {
		this.detailsService = detailsService;
	}
}
