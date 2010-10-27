package org.flexpay.common.actions.security.opensso;

import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.sun.identity.idm.IdRepoException;
import com.sun.identity.provider.springsecurity.OpenSSOAuthenticationProvider;
import com.sun.identity.provider.springsecurity.OpenSSOSimpleAuthoritiesPopulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.userdetails.User;
import org.springframework.security.userdetails.UserDetails;

public class AuthenticationProvider extends OpenSSOAuthenticationProvider {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private AuthenticationUserDetailsService authenticationUserDetailsService;

	/**
	 * Authenticate the access request.
	 *
	 * Note by this point the user has already been granted an sso token
	 * (i.e. they have already authenticated because they were redirected
	 * to opensso).
	 *
	 * If the user has any group membership we turn those into
	 * GrantedAuthortities (roles in Spring terminology).
	 * @see  com.sun.identity.provider.springsecurity.OpenSSOSimpleAuthoritiesPopulator
	 *
	 * Note that a failure to retrieve OpenSSO roles does not result in
	 * an non recoverable exception (but we should revisit this decision). In theory
	 * we can continue with authentication only. The user will have no
	 * GrantedAuthorities.
	 *
	 * @param authentication Authentication
	 * @return authentication token - possibly with ROLE_*  authorities.
	 *
	 * @throws org.springframework.security.AuthenticationException
	 */
    @Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		log.debug("Authentication object: {}", authentication);

		OpenSSOSimpleAuthoritiesPopulator populator = new OpenSSOSimpleAuthoritiesPopulator();

		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		String principal =  (String) token.getPrincipal();

		// hack alert
		// We pass in the SSOToken as the credential (.e.g the password)
		// this is probably confusing - and we should refactor to use a
		// proper OpenSSOAuthenticationToken.
		SSOToken ssoToken = (SSOToken) token.getCredentials();

		try {
			GrantedAuthority ga[] = populator.getGrantedAuthorities(ssoToken);
			UserDetails u = new User(principal, "secret", true,  true, true, true, ga);
			authentication = new UsernamePasswordAuthenticationToken(u, "secret", ga);

			u = authenticationUserDetailsService.loadUserDetails(authentication);
			authentication = new UsernamePasswordAuthenticationToken(u, "secret", ga);
		} catch (IdRepoException ex) {
			log.error("Authentication error: " + ex.getMessage(), ex);
		} catch (SSOException ex) {
			log.error("Authentication error: " + ex.getMessage(), ex);
		}

		return authentication;
	}

    @Required
	public void setAuthenticationUserDetailsService(AuthenticationUserDetailsService authenticationUserDetailsService) {
		this.authenticationUserDetailsService = authenticationUserDetailsService;
	}
}
