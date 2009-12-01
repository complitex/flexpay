package org.flexpay.ab.action.building;

import org.flexpay.ab.actions.buildings.BuildingAddressesListAction;
import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleBuilding;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestBuildingAddressesListAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingAddressesListAction action;
	@Autowired
	private BuildingDao buildingDao;

	@Test
	public void testAction() throws Exception {

		action.setBuilding(new Building(TestData.IVANOVA_27));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid building addresses list size", action.getAddresses().size() > 0);

	}

	@Test
	public void testIncorrectBuildingId() throws Exception {

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

		buildingDao.delete(action.getBuilding());

	}

}
