package org.flexpay.ab.action.building;

import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.dao.BuildingsDao;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimpleBuilding;
import static org.junit.Assert.*;

public class TestBuildingAddressesListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingAddressesListAction action;
	@Autowired
	private BuildingDao buildingDao;
	@Autowired
	private BuildingsDao buildingsDao;

	@Test
	public void testAction() throws Exception {

		action.setBuilding(new Building(TestData.IVANOVA_27));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertFalse("Invalid building addresses list size", action.getAddresses().isEmpty());

	}

	@Test
	public void testNullBuildingId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullBuilding() throws Exception {

		action.setBuilding(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectBuildingId1() throws Exception {

		action.setBuilding(new Building(-122L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectBuildingId2() throws Exception {

		action.setBuilding(new Building(0L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctBuilding() throws Exception {

		action.setBuilding(new Building(1090772L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDisabledBuilding() throws Exception {

		Building building = createSimpleBuilding("12312");
		building.disable();
		for (BuildingAddress address : building.getBuildingses()) {
			address.disable();
		}

		buildingDao.create(building);

		action.setBuilding(building);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		buildingDao.delete(building);

	}

}
