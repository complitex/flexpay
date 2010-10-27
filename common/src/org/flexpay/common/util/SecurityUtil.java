package org.flexpay.common.util;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;

import java.util.List;

public abstract class SecurityUtil {

	private static final Logger log = LoggerFactory.getLogger(SecurityUtil.class);

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

	/**
	 * Check if current authentication has authority with required name
	 *
	 * @param authName Authority name to check
	 * @return <code>true</code> if authority found, or <code>false</code> otherwise
	 */
	public static boolean isAuthenticationGranted(@NotNull String authName) {

		Authentication auth = getAuthentication();
		if (auth == null) {
			return false;
		}

		GrantedAuthority[] authorities = auth.getAuthorities();
		if (authorities == null) {
			return false;
		}

		for (GrantedAuthority authority : authorities) {
			if (authName.equals(authority.getAuthority())) {
				return true;
			}
		}

		return false;
	}
}
