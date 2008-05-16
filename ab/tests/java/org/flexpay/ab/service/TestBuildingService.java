package org.flexpay.ab.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAttribute;
import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Street;
import org.flexpay.common.test.SpringBeanAwareTestCase;

public class TestBuildingService extends SpringBeanAwareTestCase {

	/**
	 * Override to run the test and assert its state.
	 * 
	 * @throws Throwable
	 *             if any exception is thrown
	 */
	@Override
	protected void runTest() throws Throwable {
		testCreateBuilding();
		testFindBuildings();
		testFindBulkBuildings();
	}

	public void testCreateBuilding() throws Throwable {
		BuildingDao buildingDao = (BuildingDao) applicationContext
				.getBean("buildingDao");
		BuildingService buildingService = (BuildingService) applicationContext
				.getBean("buildingService");

		Building building = new Building();
		building.setDistrict(new District(1L));

		Buildings buildings = new Buildings();
		buildings.setBuilding(building);
		buildings.setStreet(new Street(1L));
		buildings.setStatus(Buildings.STATUS_ACTIVE);

		BuildingAttribute number = new BuildingAttribute();
		number.setBuildingAttributeType(buildingService
				.getAttributeType(BuildingAttributeType.TYPE_NUMBER));
		number.setValue("Test number #123");
		number.setBuildings(buildings);

		BuildingAttribute bulkNumber = new BuildingAttribute();
		bulkNumber.setBuildingAttributeType(buildingService
				.getAttributeType(BuildingAttributeType.TYPE_BULK));
		bulkNumber.setValue("Test bulk number #1");
		bulkNumber.setBuildings(buildings);

		Set<BuildingAttribute> attributes = new HashSet<BuildingAttribute>();
		attributes.add(number);
		attributes.add(bulkNumber);
		buildings.setBuildingAttributes(attributes);

		Set<Buildings> buildingses = new HashSet<Buildings>();
		buildingses.add(buildings);
		building.setBuildingses(buildingses);

		buildingDao.create(building);

		assertNotNull("Building create failed", building.getId());
		assertNotNull("Buildings create failed", buildings.getId());
		assertNotNull("Buildings number create failed", number.getId());
		assertNotNull("Buildings bulk number create failed", bulkNumber.getId());

		buildingDao.delete(building);
	}

	public void testFindBulkBuildings() throws Throwable {
		BuildingService buildingService = (BuildingService) applicationContext
				.getBean("buildingService");

		// See init_db script
		Buildings buildings = buildingService.findBuildings(new Street(2L),
				new District(9L), "31", "2");

		assertNotNull("Building find with bulk number faild", buildings);
		assertEquals("Invalid number", "31", buildings.getNumber());
		assertEquals("Invalid number", "2", buildings.getBulk());
	}

	public void testFindBuildings() throws Throwable {
		BuildingService buildingService = (BuildingService) applicationContext
				.getBean("buildingService");

		// See init_db script
		Buildings buildings = buildingService.findBuildings(new Street(2L),
				new District(9L), "31", "");

		assertNotNull("Building find faild", buildings);
		assertEquals("Invalid building number", "31", buildings.getNumber());
		assertTrue("Not empty bulk number", StringUtils.isEmpty(buildings
				.getBulk()));
	}
}
