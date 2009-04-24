package org.flexpay.eirc.test;

import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.service.Roles.*;
import static org.flexpay.orgs.service.Roles.*;
import static org.flexpay.payments.service.Roles.*;
import static org.flexpay.ab.service.Roles.TOWN_TYPE_READ;
import static org.flexpay.ab.service.Roles.STREET_TYPE_READ;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.eirc.service.Roles;
import org.junit.BeforeClass;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;

public class EircSpringBeanAwareTestCase extends AbSpringBeanAwareTestCase {

	/**
	 * Authenticate test user
	 */
	@BeforeClass
	public static void authenticateTestUser() {
		GrantedAuthority[] authorities = SecurityUtil.auths(
				Roles.QUITTANCE_READ,
				APARTMENT_READ,
				BUILDING_ATTRIBUTE_TYPE_READ,
				BUILDING_READ,
				STREET_READ,
				DISTRICT_READ,
				TOWN_READ,
				REGION_READ,
				TOWN_TYPE_READ,
				STREET_TYPE_READ,
				SERVICE_READ,
				SERVICE_TYPE_READ,
				SERVICE_ORGANIZATION_READ
		);
		User user = new User("test", "test", true, true, true, true, authorities);
		Authentication auth = new AnonymousAuthenticationToken("key", user, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
