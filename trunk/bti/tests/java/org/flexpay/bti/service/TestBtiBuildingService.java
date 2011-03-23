package org.flexpay.bti.service;

import org.flexpay.ab.persistence.TestData;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

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
