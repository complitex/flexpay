package org.flexpay.ab.service;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.AddressAttributeDao;
import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.dao.BuildingsDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.ab.util.config.ApplicationConfig;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.transaction.annotation.Transactional;

public class TestBuildingService extends AbSpringBeanAwareTestCase {

	@Autowired
	protected BuildingDao buildingDao;
	@Autowired
	protected BuildingsDao buildingsDao;
	@Autowired
	protected AddressAttributeDao addressAttributeDao;
	protected BuildingService buildingService;

	@Autowired
	public void setBuildingService(@Qualifier ("buildingService") BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	// See init_db script
	private Street street = new Street(2L);
	private District district = new District(9L);

	private AddressAttributeType numberType;
	private AddressAttributeType bulkType;

	private Building newBuilding() {

		Building building = new Building();
		building.setDistrict(new District(1L));
		return building;
	}

	private BuildingAddress newBuildings(Building building) {

		BuildingAddress buildingAddress = new BuildingAddress();
		buildingAddress.setStreet(street);
		building.addAddress(buildingAddress);
		return buildingAddress;
	}

	@NotTransactional
	@Test
	public void testDeleteAttributeNotTx() throws Throwable {

		Building building = newBuilding();
		BuildingAddress buildingAddress = newBuildings(building);

		buildingAddress.setBuildingAttribute("#123", numberType);
		buildingAddress.setBuildingAttribute("#1", bulkType);

		buildingDao.create(building);

		try {
			AddressAttribute bulk = buildingAddress.setBuildingAttribute(null, bulkType);
			assertNotNull("Bulk is NULL!", bulk);
//			buildingsDao.update(buildings);
			addressAttributeDao.delete(bulk);

			buildingAddress = buildingsDao.readFull(buildingAddress.getId());
			assertEquals("BuildingAddress attribute delete method failed", 1, buildingAddress.getBuildingAttributes().size());

			assertTrue("BuildingAddress attribute delete persistence failed", StringUtils.isEmpty(buildingAddress.getBulk()));
		} finally {
			buildingDao.delete(buildingDao.readFull(building.getId()));
		}
	}

	@Transactional
	@Test
	@Ignore
	public void testDeleteAttributeTx() throws Throwable {

		Building building = newBuilding();
		BuildingAddress buildingAddress = newBuildings(building);

		buildingAddress.setBuildingAttribute("#123", numberType);
		buildingAddress.setBuildingAttribute("#1", bulkType);

		buildingDao.create(building);

		try {
			buildingAddress.setBuildingAttribute(null, bulkType);
			buildingsDao.update(buildingAddress);

			buildingAddress = buildingsDao.readFull(buildingAddress.getId());
			assertEquals("BuildingAddress attribute delete method failed", 1, buildingAddress.getBuildingAttributes().size());

			assertTrue("BuildingAddress attribute delete persistence failed", StringUtils.isEmpty(buildingAddress.getBulk()));
		} finally {
			buildingDao.delete(buildingDao.readFull(building.getId()));
		}
	}

	@Test
	@NotTransactional
	public void testCreateBuilding() throws Throwable {

		Building building = newBuilding();
		BuildingAddress buildingAddress = newBuildings(building);

		AddressAttribute number = buildingAddress.setBuildingAttribute("Test number #123", numberType);
		AddressAttribute bulk = buildingAddress.setBuildingAttribute("Test bulk number #1", bulkType);

		buildingDao.create(building);

		try {
			assertNotNull("Building create failed", building.getId());
			assertNotNull("BuildingAddress create failed", buildingAddress.getId());
			assertNotNull("BuildingAddress number create failed", number.getId());
			assertNotNull("BuildingAddress bulk number create failed", bulk.getId());
		} finally {
			buildingDao.delete(building);
		}

	}

	@Test
	@NotTransactional
	public void testFindBulkBuildings() throws Throwable {

		// See init_db script
		AddressAttribute number = BuildingAddress.numberAttribute("31");
		AddressAttribute bulk = BuildingAddress.bulkAttribute("2");
		BuildingAddress buildingAddress = buildingService.findBuildings(stub(street), stub(district), set(number, bulk));

		assertNotNull("Building find with bulk number faild", buildingAddress);
		assertEquals("Invalid number", "31", buildingAddress.getNumber());
		assertEquals("Invalid number", "2", buildingAddress.getBulk());
	}

	@Test
	@NotTransactional
	public void testFindBuildings() throws Throwable {

		// See init_db script
		AddressAttribute number = BuildingAddress.numberAttribute("31");
		BuildingAddress buildingAddress = buildingService.findBuildings(stub(street), stub(district), set(number));

		assertNotNull("Building find faild", buildingAddress);
		assertEquals("Invalid building number", "31", buildingAddress.getNumber());
		assertTrue("Not empty bulk number", StringUtils.isEmpty(buildingAddress.getBulk()));
	}

	@Test
	public void testFindBuildingAttributeTypes() {
		assertNotNull("Number attribute type not found",
				stub(ApplicationConfig.getBuildingAttributeTypeNumber()));
		assertNotNull("Bulk attribute type not found",
				stub(ApplicationConfig.getBuildingAttributeTypeBulk()));
	}

	@Before
	public void prepare() throws Exception {
		numberType = ApplicationConfig.getBuildingAttributeTypeNumber();
		bulkType = ApplicationConfig.getBuildingAttributeTypeBulk();
	}
}
