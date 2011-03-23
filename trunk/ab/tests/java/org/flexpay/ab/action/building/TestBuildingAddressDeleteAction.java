package org.flexpay.ab.action.building;

import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.dao.BuildingsDao;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimpleBuilding;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.*;

public class TestBuildingAddressDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingAddressDeleteAction action;
	@Autowired
	private BuildingDao buildingDao;
	@Autowired
	private BuildingsDao buildingAddressDao;

	@Test
	public void testIncorrectBuildingId1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectBuildingId2() throws Exception {

		action.setBuilding(new Building(-10L));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

	}

	@Test
	public void testIncorrectBuildingId3() throws Exception {

		action.setBuilding(new Building(0L));

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
	public void testNullObjectIds() throws Exception {

		Building building = createSimpleBuilding("123499");
		buildingDao.create(building);

		action.setBuilding(building);
		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		buildingDao.delete(building);

	}

	@Test
	public void testAction() throws Exception {

		Building building = createSimpleBuilding("123324");
		buildingDao.create(building);

		action.setBuilding(building);
		Long addressId = building.getAddressOnStreet(TestData.DEMAKOVA).getId();
		action.setObjectIds(set(addressId, -210L, 23455L, 0L, null));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
		assertTrue("Invalid status for building address. Must be disabled", buildingAddressDao.read(addressId).isNotActive());

		buildingDao.delete(building);

	}

}
