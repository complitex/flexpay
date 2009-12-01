package org.flexpay.ab.action.building;

import org.flexpay.ab.actions.buildings.BuildingAddressDeleteAction;
import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.dao.BuildingsDao;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleBuilding;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestBuildingAddressDeleteAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingAddressDeleteAction action;
	@Autowired
	private BuildingDao buildingDao;
	@Autowired
	private BuildingsDao buildingAddressDao;

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
	public void testNullObjectIds() throws Exception {

		Building building = createSimpleBuilding("123499");
		buildingDao.create(building);

		action.setBuilding(building);
		action.setObjectIds(null);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		buildingDao.delete(building);

	}

	@Test
	public void testAction() throws Exception {

		Building building = createSimpleBuilding("123324");
		buildingDao.create(building);

		action.setBuilding(building);
		Long addressId = building.getAddressOnStreet(TestData.DEMAKOVA).getId();
		action.setObjectIds(set(addressId));

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());

		assertTrue("Invalid status for building address. Must be disabled", buildingAddressDao.read(addressId).isNotActive());

		buildingDao.delete(building);

	}

}
