package org.flexpay.payments.test;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.config.UserPreferences;
import static org.flexpay.ab.service.Roles.*;
import static org.flexpay.orgs.service.Roles.*;
import static org.flexpay.payments.service.Roles.*;
import org.junit.BeforeClass;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;

public class PaymentsSpringBeanAwareTestCase extends SpringBeanAwareTestCase {

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
				PAYMENTS_REPORT,
				SERVICE_PROVIDER_READ,
				SERVICE_ORGANIZATION_READ,
				BANK_READ,
				PAYMENTS_COLLECTOR_READ,
				SERVICE_TYPE_READ,
				ORGANIZATION_READ,
				OPERATION_READ,
				PAYMENTS_REPORT,
				SERVICE_READ,
				CASHBOX_READ,
				PAYMENT_POINT_READ,
				OPERATION_READ,
				DOCUMENT_READ,
				DOCUMENT_TYPE_READ,
				DOCUMENT_TYPE_DELETE,
				ORGANIZATION_ADD,
				SERVICE_ORGANIZATION_ADD,
				SERVICE_PROVIDER_ADD,
				SERVICE_TYPE_ADD,
				SERVICE_TYPE_CHANGE,
				SERVICE_ADD,
				SERVICE_CHANGE,
				PAYMENTS_COLLECTOR_ADD,
				PAYMENT_POINT_ADD,
				CASHBOX_ADD,
				CASHBOX_CHANGE,
				OPERATION_ADD,
				OPERATION_CHANGE,
				DOCUMENT_ADD
		);
		User user = new User("test", "test", true, true, true, true, authorities);
		UserPreferences preferences = new UserPreferences();
		preferences.setTargetDetails(user);
		Authentication auth = new AnonymousAuthenticationToken("key", preferences, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
