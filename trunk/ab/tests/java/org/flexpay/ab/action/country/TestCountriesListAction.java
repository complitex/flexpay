package org.flexpay.ab.action.country;

import org.flexpay.ab.actions.country.CountriesListAction;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestCountriesListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private CountriesListAction action;

	@Test
	public void testAction() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid countries list size", action.getCountries().size() > 0);

	}

	@Test
	public void testIncorrectSorter() throws Exception {

		action.setCountrySorter(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid countries list size", action.getCountries().size() > 0);

	}

}
