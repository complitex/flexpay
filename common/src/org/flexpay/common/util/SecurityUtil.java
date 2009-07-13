package org.flexpay.common.util;

import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;

import java.util.List;

public abstract class SecurityUtil {

	/**
	 * Retrive current user name
	 *
	 * @return Current authenticated user name
	 */
	public static String getUserName() {
		Authentication auth = getAuthentication();
		return auth != null ? auth.getName() : null;
	}

	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static void setAuthentication(Authentication auth) {
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	/**
	 * Authenticate user with a list of authorities
	 *
	 * @param userName	User name to authenticate
	 * @param authorities List of authorities
	 */
	public static void authenticate(String userName, List<String> authorities) {

		GrantedAuthority[] grantedAuthorities = new GrantedAuthority[authorities.size()];
		int n = 0;
		for (String authority : authorities) {
			grantedAuthorities[n] = new GrantedAuthorityImpl(authority);
			++n;
		}

		User user = new User(userName, "", true, true, true, true, grantedAuthorities);
		Authentication auth = new AnonymousAuthenticationToken("key", user, grantedAuthorities);
		setAuthentication(auth);
	}

	public static GrantedAuthority auth(String authName) {
		return new GrantedAuthorityImpl(authName);
	}

	public static GrantedAuthority[] auths(String... authNames) {
		List<GrantedAuthority> authorities = CollectionUtils.list();
		for (String auth : authNames) {
			authorities.add(auth(auth));
		}

		return authorities.toArray(new GrantedAuthority[authorities.size()]);
	}
}
