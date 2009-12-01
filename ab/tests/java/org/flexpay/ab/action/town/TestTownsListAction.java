package org.flexpay.ab.action.town;

import org.flexpay.ab.actions.town.TownsListAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestTownsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private TownsListAction action;

	@Test
	public void testAction() throws Exception {

		action.setRegionFilter(TestData.REGION_NSK.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid towns list size", action.getTowns().size() > 0);

	}

	@Test
	public void testIncorrectRegionFilter1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Towns list size must be 0", action.getTowns().size() > 0);

	}

	@Test
	public void testIncorrectRegionFilter2() throws Exception {

		action.setRegionFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Towns list size must be 0", action.getTowns().size() > 0);

	}

	@Test
	public void testIncorrectSorterByName() throws Exception {

		action.setRegionFilter(TestData.REGION_NSK.getId());
		action.setTownSorterByName(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid towns list size", action.getTowns().size() > 0);

	}

	@Test
	public void testIncorrectSorterByType() throws Exception {

		action.setRegionFilter(TestData.REGION_NSK.getId());
		action.setTownSorterByType(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid towns list size", action.getTowns().size() > 0);

	}

}
