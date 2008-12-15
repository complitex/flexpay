package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.common.test.SecureSpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;

public class TestSecurity extends SecureSpringBeanAwareTestCase {

	private Authentication authentication;

	private static final GrantedAuthority[] BASIC_AUTHORITIES = {new GrantedAuthorityImpl(Roles.BASIC)};
	private static final GrantedAuthority[] COUNTRY_AUTHORITIES = {new GrantedAuthorityImpl(Roles.COUNTRY_READ)};
	private static final GrantedAuthority[] REGION_AUTHORITIES = {new GrantedAuthorityImpl(Roles.REGION_READ)};

	@Autowired
	@Qualifier ("countryService")
	private CountryService countryService;
	@Autowired
	@Qualifier ("regionService")
	private RegionService regionService;

	@Before
	public void getAuthentication() {
		authentication = SecurityContextHolder.getContext().getAuthentication();
	}

	@After
	public void setAuthentication() {
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Test (expected = AccessDeniedException.class)
	public void testSecurityCheckFailure() throws Exception {

		User user = new User("test", "test", true, true, true, true, BASIC_AUTHORITIES);
		Authentication auth = new AnonymousAuthenticationToken("key", user, BASIC_AUTHORITIES);
		SecurityContextHolder.getContext().setAuthentication(auth);

		countryService.getCountries(ApplicationConfig.getDefaultLocale());
	}

	@Test
	public void testSecurityCheckValid() throws Exception {

		User user = new User("test", "test", true, true, true, true, COUNTRY_AUTHORITIES);
		Authentication auth = new AnonymousAuthenticationToken("key", user, COUNTRY_AUTHORITIES);
		SecurityContextHolder.getContext().setAuthentication(auth);

		countryService.getCountries(ApplicationConfig.getDefaultLocale());
	}

	@Test (expected = AccessDeniedException.class)
	public void testInvalidRegionAuthorities() throws Exception {

		User user = new User("test", "test", true, true, true, true, REGION_AUTHORITIES);
		Authentication auth = new AnonymousAuthenticationToken("key", user, REGION_AUTHORITIES);
		SecurityContextHolder.getContext().setAuthentication(auth);

		ArrayStack filters = CollectionUtils.arrayStack(
				new CountryFilter(),
				new RegionFilter());
		regionService.initFilters(filters, ApplicationConfig.getDefaultLocale());
	}
}
