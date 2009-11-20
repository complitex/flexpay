package org.flexpay.ab.test;

import static org.flexpay.ab.service.Roles.*;
import org.flexpay.ab.util.config.AbUserPreferences;
import static org.flexpay.common.service.Roles.PROCESS_READ;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.junit.Before;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {
		"ab-beans.xml"
})
public class AbSpringBeanAwareTestCase extends SpringBeanAwareTestCase {

	/**
	 * Authenticate test user
	 */
	@Before
	@Override
	public void authenticateTestUser() {
		GrantedAuthority[] authorities = SecurityUtil.auths(
				COUNTRY_READ,
				COUNTRY_ADD,
				COUNTRY_CHANGE,
				COUNTRY_DELETE,
				REGION_READ,
				REGION_ADD,
				REGION_CHANGE,
				REGION_DELETE,
				TOWN_READ,
				TOWN_ADD,
				TOWN_CHANGE,
				TOWN_DELETE,
				DISTRICT_READ,
				DISTRICT_ADD,
				DISTRICT_CHANGE,
				DISTRICT_DELETE,
				STREET_READ,
				STREET_ADD,
				STREET_CHANGE,
				STREET_DELETE,
				BUILDING_READ,
				BUILDING_ADD,
				BUILDING_CHANGE,
				BUILDING_DELETE,
				APARTMENT_READ,
				APARTMENT_ADD,
				BUILDING_ATTRIBUTE_TYPE_READ,
				STREET_TYPE_READ,
				STREET_TYPE_ADD,
				STREET_TYPE_CHANGE,
				IDENTITY_TYPE_READ,
				PERSON_READ,
				PERSON_ADD,
				PERSON_CHANGE,
				PROCESS_READ,
				TOWN_TYPE_READ,
                TOWN_TYPE_ADD,
                TOWN_TYPE_DELETE,
                TOWN_TYPE_CHANGE
		);
		User user = new User("test", "test", true, true, true, true, authorities);
		AbUserPreferences preferences = new AbUserPreferences();
		preferences.setTargetDetails(user);
		Authentication auth = new AnonymousAuthenticationToken("key", preferences, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

}
