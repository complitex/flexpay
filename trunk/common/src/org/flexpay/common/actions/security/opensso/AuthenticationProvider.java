package org.flexpay.common.actions.security.opensso;

import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.User;
import org.springframework.security.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sun.identity.provider.springsecurity.OpenSSOSimpleAuthoritiesPopulator;
import com.sun.identity.provider.springsecurity.OpenSSOAuthenticationProvider;
import com.iplanet.sso.SSOToken;

public class AuthenticationProvider extends OpenSSOAuthenticationProvider {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private AuthenticationUserDetailsService authenticationUserDetailsService;

	/**
	 * authenticate the access request.
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
	 * @param authentication
	 * @return authentication token - possibly with ROLE_*  authorities.
	 *
	 * @throws org.springframework.security.AuthenticationException
	 */
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		log.error("authenticate: " + authentication, new RuntimeException());

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
		} catch (Exception ex) {
			 //throw new AuthenticationServiceException("Exception trying to get AMIdentity", ex);
			// Note: We eat the exception
			// The authentication can still succeed - but there will be no
			// granted authorities (i.e. no roles granted).
			// This is arguably the right thing to do here
			log.error("Exception Trying to get AMIdentity", ex);
		}

		return authentication;
	}

	public void setAuthenticationUserDetailsService(AuthenticationUserDetailsService authenticationUserDetailsService) {
		this.authenticationUserDetailsService = authenticationUserDetailsService;
	}
}
