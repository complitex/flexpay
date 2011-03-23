package org.flexpay.ab.action.apartment;

import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.action.FPActionSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.flexpay.ab.util.TestUtils.createSimpleBuilding;
import static org.junit.Assert.*;

public class TestApartmentsListPageAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private ApartmentsListPageAction action;
	@Autowired
	private BuildingDao buildingDao;

	@Test
	public void testIncorrectFilterValue1() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of buildingFilter in user preferences", new Long(0), up.getBuildingFilter());

	}

	@Test
	public void testIncorrectFilterValue2() throws Exception {

		action.setBuildingFilter(-100L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of buildingFilter in user preferences", new Long(0), up.getBuildingFilter());

	}

	@Test
	public void testDefunctBuilding() throws Exception {

		action.setBuildingFilter(234334L);

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of buildingFilter in user preferences", new Long(0), up.getBuildingFilter());

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

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of buildingFilter in user preferences", new Long(0), up.getBuildingFilter());

		buildingDao.delete(building);

	}

	@Test
	public void testAction() throws Exception {

		action.setBuildingFilter(TestData.ADDR_IVANOVA_27.getId());

		assertEquals("Invalid action result", FPActionSupport.SUCCESS, action.execute());
		assertFalse("Invalid action execute: has action errors.", action.hasActionErrors());

		AbUserPreferences up = (AbUserPreferences) action.getUserPreferences();
		assertEquals("Invalid value of buildingFilter in user preferences", TestData.ADDR_IVANOVA_27.getId(), up.getBuildingFilter());

	}

}
