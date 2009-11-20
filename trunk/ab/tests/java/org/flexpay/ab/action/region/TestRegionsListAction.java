package org.flexpay.ab.action.region;

import org.flexpay.ab.actions.region.RegionsListAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRegionsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private RegionsListAction action;

	@Test
	public void testAction() throws Exception {

		action.setCountryFilter(TestData.COUNTRY_RUS.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

	}

	@Test
	public void testIncorrectCountryFilter1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.ERROR, action.execute());

	}

	@Test
	public void testIncorrectCountryFilter2() throws Exception {

		action.setCountryFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.ERROR, action.execute());

	}

	@Test
	public void testIncorrectSorter() throws Exception {

		action.setCountryFilter(TestData.COUNTRY_RUS.getId());
		action.setRegionSorter(null);

		assertEquals("Invalid action result", FPActionSupport.ERROR, action.execute());

	}

}
