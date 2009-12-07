package org.flexpay.ab.action.street;

import org.flexpay.ab.actions.street.StreetsListAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestStreetsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private StreetsListAction action;

	@Test
	public void testAction() throws Exception {

		action.setTownFilter(TestData.TOWN_NSK.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid streets list size", action.getStreets().isEmpty());

	}

	@Test
	public void testIncorrectTownFilter1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Streets list size must be 0", action.getStreets().isEmpty());

	}

	@Test
	public void testIncorrectTownFilter2() throws Exception {

		action.setTownFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Streets list size must be 0", action.getStreets().isEmpty());

	}

	@Test
	public void testIncorrectSorterByName() throws Exception {

		action.setTownFilter(TestData.TOWN_NSK.getId());
		action.setStreetSorterByName(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid streets list size", action.getStreets().isEmpty());

	}

	@Test
	public void testIncorrectSorterByType() throws Exception {

		action.setTownFilter(TestData.TOWN_NSK.getId());
		action.setStreetSorterByType(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid streets list size", action.getStreets().isEmpty());

	}

}
