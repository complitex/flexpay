package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.Region;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.sorter.CountrySorter;
import org.flexpay.ab.persistence.sorter.RegionSorter;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.security.userdetails.User;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;

public class TestSecurity extends AbSpringBeanAwareTestCase {

	private Authentication authentication;

	private static final GrantedAuthority[] BASIC_AUTHORITIES = {new GrantedAuthorityImpl(Roles.BASIC)};
	private static final GrantedAuthority[] COUNTRY_AUTHORITIES = {new GrantedAuthorityImpl(Roles.COUNTRY_READ)};
	private static final GrantedAuthority[] REGION_AUTHORITIES = {new GrantedAuthorityImpl(Roles.REGION_READ)};

	@Autowired
	private CountryService countryService;
	@Autowired
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

        ArrayStack filters = arrayStack();
		List<ObjectSorter> sorters = list(new CountrySorter().activate());
		Page<Country> pager = new Page<Country>();
		countryService.find(filters, sorters, pager);
	}

	@Test
	public void testSecurityCheckValid() throws Exception {

		User user = new User("test", "test", true, true, true, true, COUNTRY_AUTHORITIES);
		Authentication auth = new AnonymousAuthenticationToken("key", user, COUNTRY_AUTHORITIES);
		SecurityContextHolder.getContext().setAuthentication(auth);

		ArrayStack filters = arrayStack();
		List<ObjectSorter> sorters = list(new CountrySorter().activate());
		Page<Country> pager = new Page<Country>();
		countryService.find(filters, sorters, pager);
	}

	@Test
	public void testRegionAuthorities() throws Exception {

		User user = new User("test", "test", true, true, true, true, REGION_AUTHORITIES);
		Authentication auth = new AnonymousAuthenticationToken("key", user, REGION_AUTHORITIES);
		SecurityContextHolder.getContext().setAuthentication(auth);

		ArrayStack filters = arrayStack(new CountryFilter(ApplicationConfig.getDefaultCountryStub()));
		List<ObjectSorter> sorters = list(new RegionSorter().activate());
		Page<Region> pager = new Page<Region>();
		regionService.find(filters, sorters, pager);
	}
}
