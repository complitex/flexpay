package org.flexpay.bti.service;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.bti.persistence.BtiBuilding;
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

		BtiBuilding building = buildingService.readWithAttributes(new Stub<BtiBuilding>(1L));
		assertNotNull("Building not found", building);
	}

	@Test
	public void testGetBuildingWithAttributesByAddress() {

		BtiBuilding building = buildingService.readWithAttributesByAddress(new Stub<BuildingAddress>(1L));
		assertNotNull("Building not found by address", building);
	}
}
