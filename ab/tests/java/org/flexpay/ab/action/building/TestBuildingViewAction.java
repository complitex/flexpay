package org.flexpay.ab.action.building;

import org.flexpay.ab.actions.buildings.BuildingViewAction;
import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.dao.BuildingDaoExt;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleBuilding;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestBuildingViewAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingViewAction action;
	@Autowired
	private BuildingDao buildingDao;
	@Autowired
	private BuildingDaoExt buildingDaoExt;

	@Test
	public void testCorrectData() throws Exception {

		action.setBuilding(new Building(TestData.IVANOVA_27));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
	}

	@Test
	public void testNullId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testIncorrectId1() throws Exception {

		action.setBuilding(new Building(-10L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testIncorrectId2() throws Exception {

		action.setBuilding(new Building(0L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testNullBuilding() throws Exception {

		action.setBuilding(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testDefunctBuilding() throws Exception {

		action.setBuilding(new Building(1090772L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());
	}

	@Test
	public void testDisabledBuilding() throws Exception {

		Building building = createSimpleBuilding("2222");
		building.disable();
		for (BuildingAddress address : building.getBuildingses()) {
			address.disable();
		}

		buildingDao.create(building);

		action.setBuilding(building);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		buildingDaoExt.deleteBuilding(action.getBuilding());

	}

}
