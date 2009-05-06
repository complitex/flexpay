package org.flexpay.orgs.test;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.SecurityUtil;
import static org.flexpay.orgs.service.Roles.*;
import org.junit.BeforeClass;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;

public class OrgsSpringBeanAwareTestCase extends SpringBeanAwareTestCase {

	/**
	 * Authenticate test user
	 */
	@BeforeClass
	public static void authenticateTestUser() {
		GrantedAuthority[] authorities = SecurityUtil.auths(
				ORGANIZATION_READ
		);
		User user = new User("test", "test", true, true, true, true, authorities);
		Authentication auth = new AnonymousAuthenticationToken("key", user, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
