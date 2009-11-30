package org.flexpay.ab.action.district;

import org.flexpay.ab.actions.district.DistrictsListAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDistrictsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private DistrictsListAction action;

	@Test
	public void testAction() throws Exception {

		action.setTownFilter(TestData.TOWN_NSK.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid districts list size", action.getDistricts().size() > 0);

	}

	@Test
	public void testIncorrectTownFilter1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Districts list size must be 0", action.getDistricts().size() > 0);

	}

	@Test
	public void testIncorrectTownFilter2() throws Exception {

		action.setTownFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Districts list size must be 0", action.getDistricts().size() > 0);

	}

	@Test
	public void testIncorrectSorter() throws Exception {

		action.setTownFilter(TestData.TOWN_NSK.getId());
		action.setDistrictSorter(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid districts list size", action.getDistricts().size() > 0);

	}

}
