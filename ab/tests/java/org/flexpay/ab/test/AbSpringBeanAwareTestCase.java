package org.flexpay.ab.test;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.SecurityUtil;
import static org.flexpay.ab.service.Roles.*;
import org.junit.BeforeClass;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;

public class AbSpringBeanAwareTestCase extends SpringBeanAwareTestCase {

	/**
	 * Authenticate test user
	 */
	@BeforeClass
	public static void authenticateTestUser() {
		GrantedAuthority[] authorities = SecurityUtil.auths(
				APARTMENT_READ,
				BUILDING_ATTRIBUTE_TYPE_READ,
				BUILDING_READ,
				STREET_READ,
				TOWN_READ,
				REGION_READ
		);
		User user = new User("test", "test", true, true, true, true, authorities);
		Authentication auth = new AnonymousAuthenticationToken("key", user, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
