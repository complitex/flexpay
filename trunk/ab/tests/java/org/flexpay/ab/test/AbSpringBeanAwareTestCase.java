package org.flexpay.ab.test;

import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.SecurityUtil;
import org.junit.Before;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.flexpay.ab.service.Roles.*;
import static org.flexpay.ab.util.config.ApplicationConfig.*;
import static org.flexpay.common.service.Roles.PROCESS_READ;
import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLocale;

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
		List<GrantedAuthority> authorities = SecurityUtil.auths(
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
				APARTMENT_CHANGE,
				APARTMENT_DELETE,
				TOWN_TYPE_READ,
                TOWN_TYPE_ADD,
				TOWN_TYPE_CHANGE,
                TOWN_TYPE_DELETE,
				STREET_TYPE_READ,
				STREET_TYPE_ADD,
				STREET_TYPE_CHANGE,
				STREET_TYPE_DELETE,
				IDENTITY_TYPE_READ,
				IDENTITY_TYPE_ADD,
				IDENTITY_TYPE_CHANGE,
				IDENTITY_TYPE_DELETE,
				BUILDING_ATTRIBUTE_TYPE_READ,
				BUILDING_ATTRIBUTE_TYPE_ADD,
				BUILDING_ATTRIBUTE_TYPE_CHANGE,
				BUILDING_ATTRIBUTE_TYPE_DELETE,
				PERSON_READ,
				PERSON_ADD,
				PERSON_CHANGE,
				PERSON_DELETE,
				PROCESS_READ
		);
		User user = new User("test", "test", true, true, true, true, authorities);
		AbUserPreferences preferences = new AbUserPreferences();
		preferences.setTargetDetails(user);
		preferences.setLanguageCode("ru");
		preferences.setLocale(getDefaultLocale());
		preferences.setCountryFilter(getDefaultCountryStub().getId());
		preferences.setRegionFilter(getDefaultRegionStub().getId());
		preferences.setTownFilter(getDefaultTownStub().getId());
		Authentication auth = new AnonymousAuthenticationToken("key", preferences, authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

}
