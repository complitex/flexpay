package org.flexpay.ab.action.building;

import org.flexpay.ab.actions.buildings.BuildingAddressEditAction;
import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import static org.flexpay.ab.util.TestUtils.createSimpleBuilding;
import org.flexpay.common.actions.FPActionSupport;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestBuildingAddressEditAction extends AbSpringBeanAwareTestCase {

	@Autowired
	private BuildingAddressEditAction action;
	@Autowired
	private BuildingDao buildingDao;
	@Autowired
	private AddressAttributeTypeService addressAttributeTypeService;

	@Test
	public void testNullBuilding() throws Exception {

		action.setBuilding(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullBuildingId() throws Exception {

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDefunctBuilding() throws Exception {

		action.setBuilding(new Building(121212L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDisabledBuilding() throws Exception {

		Building building = createSimpleBuilding("123004");
		building.disable();
		buildingDao.create(building);

		action.setBuilding(building);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		buildingDao.delete(building);

	}

	@Test
	public void testNullAddress() throws Exception {

		action.setBuilding(new Building(TestData.IVANOVA_27));
		action.setAddress(null);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testNullAddressId() throws Exception {

		action.setBuilding(buildingDao.read(TestData.IVANOVA_27.getId()));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDefunctAddress() throws Exception {

		action.setBuilding(new Building(TestData.IVANOVA_27));
		action.setAddress(new BuildingAddress(121212L));

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

	}

	@Test
	public void testDisabledAddress() throws Exception {

		Building building = createSimpleBuilding("12134");
		buildingDao.create(building);

		BuildingAddress address = building.getAddressOnStreet(TestData.DEMAKOVA);
		address.disable();
		buildingDao.update(building);

		action.setBuilding(building);
		action.setAddress(address);

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_ERROR, action.execute());

		buildingDao.delete(building);

	}

	@Test
	public void testCreateNotSubmit() throws Exception {

		Building building = createSimpleBuilding("12334");
		buildingDao.create(building);

		action.setBuilding(building);
		action.setAddress(new BuildingAddress(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid size of attributesMap", addressAttributeTypeService.getAttributeTypes().size(), action.getAttributesMap().size());

		buildingDao.delete(building);

	}

	@Test
	public void testEditNotSubmit() throws Exception {

		Building building = createSimpleBuilding("12534");
		buildingDao.create(building);

		action.setBuilding(building);
		action.setAddress(building.getAddressOnStreet(TestData.DEMAKOVA));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid size of attributesMap", addressAttributeTypeService.getAttributeTypes().size(), action.getAttributesMap().size());
		assertEquals("Invalid street filter", TestData.DEMAKOVA.getId(), action.getStreetFilter());
		assertEquals("Invalid town filter", TestData.TOWN_NSK.getId(), action.getTownFilter());
		assertEquals("Invalid region filter", TestData.REGION_NSK.getId(), action.getRegionFilter());
		assertEquals("Invalid country filter", TestData.COUNTRY_RUS.getId(), action.getCountryFilter());

		buildingDao.delete(building);

	}

	@Test
	public void testIncorrectData1() throws Exception {

		Building building = createSimpleBuilding("12734");
		buildingDao.create(building);

		action.setBuilding(building);
		action.setAddress(new BuildingAddress(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1");
		action.setStreetFilter(null);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		buildingDao.delete(building);

	}

	@Test
	public void testIncorrectData2() throws Exception {

		Building building = createSimpleBuilding("01234");
		buildingDao.create(building);

		action.setBuilding(building);
		action.setAddress(new BuildingAddress(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1");
		action.setStreetFilter(-10L);

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		buildingDao.delete(building);

	}

	@Test
	public void testIncorrectData3() throws Exception {

		Building building = createSimpleBuilding("12034");
		buildingDao.create(building);

		action.setBuilding(building);
		action.setAddress(new BuildingAddress(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());

		action.setSubmitted("");
		action.setStreetFilter(TestData.DEMAKOVA.getId());

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertTrue("Invalid action execute: hasn't action errors.", action.hasActionErrors());

		buildingDao.delete(building);

	}

	@Test
	public void testCreateSubmit() throws Exception {

		Building building = createSimpleBuilding("3217888");
		buildingDao.create(building);

		action.setBuilding(building);
		action.setAddress(new BuildingAddress(0L));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid size of attributesMap", addressAttributeTypeService.getAttributeTypes().size(), action.getAttributesMap().size());

		action.setSubmitted("");
		action.setStreetFilter(TestData.ROSSIISKAYA.getId());
		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "1345777");

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertNotNull("Address on Rossiiskaya Street must not be null", action.getBuilding().getAddressOnStreet(TestData.ROSSIISKAYA));

		buildingDao.delete(action.getBuilding());

	}

	@Test
	public void testEditSubmit() throws Exception {

		Building building = createSimpleBuilding("123434");
		buildingDao.create(building);

		action.setBuilding(building);
		action.setAddress(building.getAddressOnStreet(TestData.DEMAKOVA));

		assertEquals("Invalid action result", FPActionSupport.INPUT, action.execute());
		assertEquals("Invalid size of attributesMap", addressAttributeTypeService.getAttributeTypes().size(), action.getAttributesMap().size());

		action.setSubmitted("");
		action.setStreetFilter(TestData.DEMAKOVA.getId());
		action.getAttributesMap().put(org.flexpay.ab.util.config.ApplicationConfig.getBuildingAttributeTypeNumber().getId(), "13451");

		assertEquals("Invalid action result", FPActionSupport.REDIRECT_SUCCESS, action.execute());
		assertEquals("Invalid building address number", "13451", action.getBuilding().getAddressOnStreet(TestData.DEMAKOVA).getNumber());

		buildingDao.delete(action.getBuilding());

	}

}
