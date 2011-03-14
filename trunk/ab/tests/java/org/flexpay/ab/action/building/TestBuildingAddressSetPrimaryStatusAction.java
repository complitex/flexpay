package org.flexpay.ab.action.building;

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

public class TestBuildingAddressSetPrimaryStatusAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingAddressSetPrimaryStatusAction action;
	@Autowired
	private BuildingDao buildingDao;

	@Test
	public void testAction() throws Exception {

		Building building = createSimpleBuilding("12312");
		buildingDao.create(building);
		assertEquals("Invalid primary building address", building.getDefaultBuildings().getStreetStub().getId(), TestData.IVANOVA.getId());

		action.setAddress(building.getAddressOnStreet(TestData.DEMAKOVA));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		building = buildingDao.readFull(building.getId());
		assertEquals("Invalid primary building address", building.getDefaultBuildings().getStreetStub().getId(), TestData.DEMAKOVA.getId());

		buildingDao.delete(building);

	}

	@Test
	public void testNullAddressId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testNullAddress() throws Exception {

		action.setAddress(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectAddressId1() throws Exception {

		action.setAddress(new BuildingAddress(-10L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectAddressId2() throws Exception {

		action.setAddress(new BuildingAddress(0L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDefunctAddress() throws Exception {

		action.setAddress(new BuildingAddress(1212210L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testDisabledBuilding() throws Exception {

		Building building = createSimpleBuilding("222211");
		building.disable();
		for (BuildingAddress address : building.getBuildingses()) {
			address.disable();
		}

		buildingDao.create(building);

		action.setAddress(building.getAddressOnStreet(TestData.DEMAKOVA));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		buildingDao.delete(building);

	}

}
