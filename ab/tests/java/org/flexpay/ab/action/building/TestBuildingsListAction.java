package org.flexpay.ab.action.building;

import org.flexpay.ab.actions.buildings.BuildingsListAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestBuildingsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingsListAction action;

	@Test
	public void testAction() throws Exception {

		action.setStreetFilter(TestData.IVANOVA.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid buildings list size", action.getBuildings().size() > 0);

	}

	@Test
	public void testIncorrectStreetFilter1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Buildings list size must be 0", action.getBuildings().size() > 0);

	}

	@Test
	public void testIncorrectStreetFilter2() throws Exception {

		action.setStreetFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Buildings list size must be 0", action.getBuildings().size() > 0);

	}

	@Test
	public void testIncorrectSorter() throws Exception {

		action.setStreetFilter(TestData.IVANOVA.getId());
		action.setBuildingsSorter(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid buildings list size", action.getBuildings().size() > 0);

	}

}
