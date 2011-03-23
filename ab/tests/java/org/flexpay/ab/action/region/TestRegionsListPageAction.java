package org.flexpay.ab.action.region;

import org.flexpay.ab.dao.CountryDao;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimpleCountry;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultCountryStub;
import static org.junit.Assert.*;

public class TestRegionsListPageAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private RegionsListPageAction action;
	@Autowired
	private CountryDao countryDao;

	@Test
	public void testIncorrectFilterValue1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of countryFilter in user preferences", getDefaultCountryStub().getId(), up.getCountryFilter());

	}

	@Test
	public void testIncorrectFilterValue2() throws Exception {

		action.setCountryFilter(-100L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of countryFilter in user preferences", getDefaultCountryStub().getId(), up.getCountryFilter());

	}

	@Test
	public void testDefunctCountry() throws Exception {

		action.setCountryFilter(234334L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of countryFilter in user preferences", getDefaultCountryStub().getId(), up.getCountryFilter());

	}

	@Test
	public void testDisabledCountry() throws Exception {

		Country country = createSimpleCountry("123");
		country.disable();
		countryDao.create(country);

		action.setCountryFilter(country.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of countryFilter in user preferences", getDefaultCountryStub().getId(), up.getCountryFilter());

		countryDao.delete(country);

	}

	@Test
	public void testAction() throws Exception {

		action.setCountryFilter(TestData.COUNTRY_USA.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of countryFilter in user preferences", TestData.COUNTRY_USA.getId(), up.getCountryFilter());

	}

}
