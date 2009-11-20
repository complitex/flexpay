package org.flexpay.ab.action.country;

import org.flexpay.ab.actions.country.CountryViewAction;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestCountryViewAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private CountryViewAction action;

	@Test
	public void testCorrectData() throws Exception {

		action.setCountry(new Country(TestData.COUNTRY_RUS.getId()));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testIncorrectId1() throws Exception {

		action.setCountry(new Country(-10L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testIncorrectId2() throws Exception {

		action.setCountry(new Country(0L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullCountry() throws Exception {

		action.setCountry(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDefunctCountry() throws Exception {

		action.setCountry(new Country(10902L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

}
