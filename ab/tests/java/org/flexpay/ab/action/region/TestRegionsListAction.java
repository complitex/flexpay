package org.flexpay.ab.action.region;

import org.flexpay.ab.dao.CountryDao;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleCountry;
import org.flexpay.common.action.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRegionsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private RegionsListAction action;
	@Autowired
	private CountryDao countryDao;

	@Test
	public void testAction() throws Exception {

		action.setCountryFilter(TestData.COUNTRY_RUS.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid regions list size", action.getRegions().isEmpty());

	}

	@Test
	public void testIncorrectSorter() throws Exception {

		action.setCountryFilter(TestData.COUNTRY_RUS.getId());
		action.setRegionSorter(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid regions list size", action.getRegions().isEmpty());

	}

	@Test
	public void testIncorrectCountryFilter1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Regions list size must be 0", action.getRegions().isEmpty());

	}

	@Test
	public void testIncorrectCountryFilter2() throws Exception {

		action.setCountryFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Regions list size must be 0", action.getRegions().isEmpty());

	}

	@Test
	public void testIncorrectCountryFilter3() throws Exception {

		action.setCountryFilter(0L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Regions list size must be 0", action.getRegions().isEmpty());

	}

	@Test
	public void testDefunctCountry() throws Exception {

		action.setCountryFilter(234334L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Regions list size must be 0", action.getRegions().isEmpty());

	}

	@Test
	public void testDisabledCountry() throws Exception {

		Country country = createSimpleCountry("123");
		country.disable();
		countryDao.create(country);

		action.setCountryFilter(country.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Regions list size must be 0", action.getRegions().isEmpty());

		countryDao.delete(country);

	}

}
