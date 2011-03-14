package org.flexpay.ab.action.apartment;

import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleBuilding;
import org.flexpay.common.action.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestApartmentsListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private ApartmentsListAction action;
	@Autowired
	private BuildingDao buildingDao;

	@Test
	public void testAction() throws Exception {

		action.setBuildingFilter(TestData.ADDR_IVANOVA_27.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid apartments list size", action.getApartments().isEmpty());

	}

	@Test
	public void testIncorrectSorter() throws Exception {

		action.setBuildingFilter(TestData.ADDR_IVANOVA_27.getId());
		action.setApartmentSorter(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid apartments list size", action.getApartments().isEmpty());

	}

	@Test
	public void testIncorrectBuildingFilter1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Apartments list size must be 0", action.getApartments().isEmpty());

	}

	@Test
	public void testIncorrectBuildingFilter2() throws Exception {

		action.setBuildingFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Apartments list size must be 0", action.getApartments().isEmpty());

	}

	@Test
	public void testIncorrectBuildingFilter3() throws Exception {

		action.setBuildingFilter(0L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Apartments list size must be 0", action.getApartments().isEmpty());

	}

	@Test
	public void testDefunctBuilding() throws Exception {

		action.setBuildingFilter(1212120L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Apartments list size must be 0", action.getApartments().isEmpty());

	}

	@Test
	public void testDisabledBuilding() throws Exception {

		Building building = createSimpleBuilding("123");
		building.disable();
		for (BuildingAddress address : building.getBuildingses()) {
			address.disable();
		}
		buildingDao.create(building);

		action.setBuildingFilter(building.getDefaultBuildings().getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
		assertTrue("Apartments list size must be 0", action.getApartments().isEmpty());

		buildingDao.delete(building);

	}

}
