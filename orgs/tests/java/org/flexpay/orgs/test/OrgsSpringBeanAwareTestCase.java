package org.flexpay.orgs.test;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.SecurityUtil;
import static org.flexpay.orgs.service.Roles.*;
import org.junit.Before;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;

public class OrgsSpringBeanAwareTestCase extends SpringBeanAwareTestCase {

	/**
	 * Authenticate test user
	 */
	@Before
	public void authenticateTestUser() {
		GrantedAuthority[] authorities = SecurityUtil.auths(
				ORGANIZATION_READ,
				ORGANIZATION_ADD,
				SERVICE_ORGANIZATION_READ,
				SERVICE_ORGANIZATION_ADD,
				SERVICE_ORGANIZATION_CHANGE,
				SERVICE_PROVIDER_READ,
				SERVICE_PROVIDER_ADD,
				SERVICE_PROVIDER_CHANGE,
				BANK_READ,
				BANK_ADD,
				BANK_CHANGE,
				PAYMENT_COLLECTOR_READ,
				PAYMENT_COLLECTOR_ADD,
				PAYMENT_COLLECTOR_CHANGE,
				PAYMENT_POINT_READ,
				PAYMENT_POINT_ADD,
				PAYMENT_POINT_CHANGE
		);
		User user = new User("test", "test", true, true, true, true, authorities);
		Authentication auth = new AnonymousAuthenticationToken("key", user, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
