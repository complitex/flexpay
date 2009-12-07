package org.flexpay.ab.action.apartment;

import org.flexpay.ab.actions.apartment.ApartmentsListAction;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestApartmentsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private ApartmentsListAction action;

	@Test
	public void testAction() throws Exception {

		action.setBuildingFilter(TestData.ADDR_IVANOVA_27.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid apartments list size", action.getApartments().isEmpty());

	}

	@Test
	public void testIncorrectBuildingFilter1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Apartments list size must be 0", action.getApartments().isEmpty());

	}

	@Test
	public void testIncorrectBuildingFilter2() throws Exception {

		action.setBuildingFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Apartments list size must be 0", action.getApartments().isEmpty());

	}

	@Test
	public void testIncorrectSorter() throws Exception {

		action.setBuildingFilter(TestData.ADDR_IVANOVA_27.getId());
		action.setApartmentSorter(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid apartments list size", action.getApartments().isEmpty());

	}

}
