package org.flexpay.common.util;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.Authentication;

public abstract class SecurityUtil {

	/**
	 * Retrive current user name
	 * 
	 * @return Current authenticated user name
	 */
	public static String getUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null ? auth.getName() : null;
	}
}
