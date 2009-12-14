package org.flexpay.ab.action.building;

import org.flexpay.ab.actions.buildings.BuildingDeleteAction;
import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.dao.BuildingDaoExt;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleBuilding;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestBuildingDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingDeleteAction action;
	@Autowired
	private BuildingDao buildingDao;
	@Autowired
	private BuildingDaoExt buildingDaoExt;

	@Test
	public void testNullObjectIds() throws Exception {

		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());
	}

	@Test
	public void testDeleteBuilding() throws Exception {

		Building building = createSimpleBuilding("3333");
		buildingDao.create(building);

		Long addressId = building.getAddressOnStreet(TestData.DEMAKOVA).getId();
		action.setObjectIds(set(addressId, -210L, 23455L, 0L, null));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		building = buildingDao.read(building.getId());
		assertTrue("Invalid status for building. Must be disabled", building.isNotActive());

		buildingDaoExt.deleteBuilding(building);

	}

}
