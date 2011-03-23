package org.flexpay.eirc.test;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.junit.Before;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.flexpay.ab.service.Roles.*;
import static org.flexpay.common.service.Roles.PROCESS_DEFINITION_UPLOAD_NEW;
import static org.flexpay.common.service.Roles.PROCESS_READ;
import static org.flexpay.eirc.service.Roles.*;
import static org.flexpay.orgs.service.Roles.*;
import static org.flexpay.payments.service.Roles.*;

@ContextConfiguration(locations = {
		"eirc-beans.xml"
})
public class EircSpringBeanAwareTestCase extends SpringBeanAwareTestCase {

	/**
	 * Authenticate test user
	 */
	@Before
    @Override
	public void authenticateTestUser() {
		List<GrantedAuthority> authorities = SecurityUtil.auths(
				QUITTANCE_READ,
				ACCOUNT_READ,
				PERSON_READ,
				APARTMENT_READ,
				OPERATION_ADD,
				BUILDING_ATTRIBUTE_TYPE_READ,
				BUILDING_READ,
				STREET_READ,
				DISTRICT_READ,
				TOWN_READ,
				REGION_READ,
				COUNTRY_READ,
				TOWN_TYPE_READ,
				STREET_TYPE_READ,
				SERVICE_READ,
				SERVICE_TYPE_READ,
				IDENTITY_TYPE_READ,
				CASHBOX_READ,
				SERVICE_ORGANIZATION_READ,
				SERVICE_PROVIDER_READ,
				PAYMENT_POINT_READ,
				BANK_READ,
				PAYMENT_COLLECTOR_READ,
				ORGANIZATION_READ,
				ACCOUNT_ADD,
				SERVICE_ORGANIZATION_ADD_SERVED_BUILDINGS,
				SERVICE_ORGANIZATION_REMOVE_SERVED_BUILDINGS,
				BUILDING_CHANGE,
				PROCESS_DEFINITION_UPLOAD_NEW,
				DOCUMENT_READ,
				DOCUMENT_TYPE_READ,
				PROCESS_READ,
                PROCESS_DEFINITION_UPLOAD_NEW
		);
		User user = new User("test", "test", true, true, true, true, authorities);
		UserPreferences preferences = new UserPreferences();
		preferences.setTargetDetails(user);
		Authentication auth = new AnonymousAuthenticationToken("key", preferences, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
}
