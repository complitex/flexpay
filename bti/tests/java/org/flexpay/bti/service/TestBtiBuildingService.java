package org.flexpay.bti.service;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestBtiBuildingService extends SpringBeanAwareTestCase {

	@Autowired
	private BtiBuildingService buildingService;

	@Test
	public void testGetBuildingWithAttributes() {

		BtiBuilding building = buildingService.readWithAttributes(TestData.IVANOVA_2);
		assertNotNull("Building not found", building);
	}

	@Test
	public void testGetBuildingWithAttributesByAddress() {

		BtiBuilding building = buildingService.readWithAttributesByAddress(TestData.ADDR_DEMAKOVA_220D);
		assertNotNull("Building not found by address", building);
	}
}
