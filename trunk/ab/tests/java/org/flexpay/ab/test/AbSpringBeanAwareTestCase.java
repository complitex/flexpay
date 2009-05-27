package org.flexpay.ab.test;

import static org.flexpay.ab.service.Roles.*;
import static org.flexpay.common.service.Roles.PROCESS_READ;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.SecurityUtil;
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
				PERSON_READ,
				APARTMENT_READ,
				BUILDING_ATTRIBUTE_TYPE_READ,
				BUILDING_READ,
				STREET_READ,
				DISTRICT_READ,
				TOWN_READ,
				REGION_READ,
				TOWN_TYPE_READ,
				STREET_TYPE_READ,
				STREET_TYPE_ADD,
				STREET_TYPE_CHANGE,
				IDENTITY_TYPE_READ,
				APARTMENT_ADD,
				DISTRICT_ADD,
				STREET_ADD,
				STREET_CHANGE,
				TOWN_ADD,
				TOWN_CHANGE,
				PERSON_ADD,
				PERSON_CHANGE,
				PROCESS_READ
		);
		User user = new User("test", "test", true, true, true, true, authorities);
		Authentication auth = new AnonymousAuthenticationToken("key", user, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
