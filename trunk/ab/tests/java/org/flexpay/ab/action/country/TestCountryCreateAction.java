package org.flexpay.ab.action.country;

import org.flexpay.ab.actions.country.CountryCreateAction;
import org.flexpay.ab.dao.CountryDao;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.initNames;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestCountryCreateAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private CountryCreateAction action;
	@Autowired
	private CountryDao countryDao;

	@Test
	public void testNotSubmit() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testNullNames() throws Exception {

		action.setNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testNullShortNames() throws Exception {

		action.setShortNames(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

	}

	@Test
	public void testSubmit() throws Exception {

		action.setSubmitted("");
		action.setNames(initNames("123"));
		action.setShortNames(initNames("321"));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());

		countryDao.delete(action.getCountry());

	}

}
