package org.flexpay.ab.action.region;

import org.flexpay.ab.actions.region.RegionsListAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRegionsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private RegionsListAction action;

	@Test
	public void testAction() throws Exception {

		action.setCountryFilter(TestData.COUNTRY_RUS.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid regions list size", action.getRegions().size() > 0);

	}

	@Test
	public void testIncorrectCountryFilter1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Regions list size must be 0", action.getRegions().size() > 0);

	}

	@Test
	public void testIncorrectCountryFilter2() throws Exception {

		action.setCountryFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Regions list size must be 0", action.getRegions().size() > 0);

	}

	@Test
	public void testIncorrectSorter() throws Exception {

		action.setCountryFilter(TestData.COUNTRY_RUS.getId());
		action.setRegionSorter(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid regions list size", action.getRegions().size() > 0);

	}

}
