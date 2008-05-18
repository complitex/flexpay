package org.flexpay.ab.service;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.dao.BuildingsDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TestBuildingService extends SpringBeanAwareTestCase {

	@Autowired
	protected BuildingDao buildingDao;
	@Autowired
	protected BuildingsDao buildingsDao;
	@Autowired
	protected BuildingService buildingService;

	// See init_db script
	private Street street = new Street(2L);
	private District district = new District(9L);

	private BuildingAttributeType numberType;
	private BuildingAttributeType bulkType;

	private Building newBuilding() {

		Building building = new Building();
		building.setDistrict(new District(1L));
		return building;
	}

	private Buildings newBuildings(Building building) {

		Buildings buildings = new Buildings();
		buildings.setStreet(street);
		building.addBuildings(buildings);
		return buildings;
	}

	@Transactional(readOnly = false)
	@Test
	public void testDeleteAttribute() throws Throwable {

		Building building = newBuilding();
		Buildings buildings = newBuildings(building);

		buildings.setBuildingAttribute("#123", numberType);
		buildings.setBuildingAttribute("#1", bulkType);

		buildingDao.create(building);

		try {
			buildings.setBuildingAttribute(null, bulkType);
			buildingsDao.update(buildings);
			assertEquals("Buildings attribute delete method failed", 1, buildings.getBuildingAttributes().size());

			assertTrue("Buildings attribute delete persistence failed", StringUtils.isEmpty(buildings.getBulk()));
		} finally {
			buildingDao.delete(building);
		}
	}

	@Test
	public void testCreateBuilding() throws Throwable {

		Building building = newBuilding();
		Buildings buildings = newBuildings(building);

		BuildingAttribute number = buildings.setBuildingAttribute("Test number #123", numberType);
		BuildingAttribute bulk = buildings.setBuildingAttribute("Test bulk number #1", bulkType);

		buildingDao.create(building);

		try {
			assertNotNull("Building create failed", building.getId());
			assertNotNull("Buildings create failed", buildings.getId());
			assertNotNull("Buildings number create failed", number.getId());
			assertNotNull("Buildings bulk number create failed", bulk.getId());
		} finally {
			buildingDao.delete(building);
		}

	}

	@Test
	public void testFindBulkBuildings() throws Throwable {

		// See init_db script
		Buildings buildings = buildingService.findBuildings(street, district, "31", "2");

		assertNotNull("Building find with bulk number faild", buildings);
		assertEquals("Invalid number", "31", buildings.getNumber());
		assertEquals("Invalid number", "2", buildings.getBulk());
	}

	@Test
	public void testFindBuildings() throws Throwable {

		// See init_db script
		Buildings buildings = buildingService.findBuildings(new Street(2L), new District(9L), "31", "");

		assertNotNull("Building find faild", buildings);
		assertEquals("Invalid building number", "31", buildings.getNumber());
		assertTrue("Not empty bulk number", StringUtils.isEmpty(buildings.getBulk()));
	}

	@Before
	public void prepare() throws Exception {
		numberType = buildingService.getAttributeType(BuildingAttributeType.TYPE_NUMBER);
		bulkType = buildingService.getAttributeType(BuildingAttributeType.TYPE_BULK);
	}
}
