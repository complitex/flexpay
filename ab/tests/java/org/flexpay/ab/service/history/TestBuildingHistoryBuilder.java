package org.flexpay.ab.service.history;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.TestData;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.*;

public class TestBuildingHistoryBuilder extends AbSpringBeanAwareTestCase {

	@Autowired
	@Qualifier ("buildingHistoryBuilder")
	private HistoryBuilder<Building> historyBuilder;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private ObjectsFactory factory;

	@Test
	public void testBuildDiff() {

		Diff diff = historyBuilder.diff(factory.newBuilding(), factory.newBuilding());
		assertTrue("Diff of two empty buildings is not empty", diff.isEmpty());
	}

	@Test
	public void testBuildDiff3() {

		Diff diff = historyBuilder.diff(null, factory.newBuilding());
		assertTrue("Diff of two empty buildings is not empty", diff.isEmpty());
	}

	@Test
	public void testBuildDiff4() {

		Building building = buildingService.readFull(TestData.IVANOVA_27);
		if (building == null) {
			throw new IllegalStateException("No building ivanova-2 found");
		}

		Diff diff = historyBuilder.diff(null, building);
		assertEquals("Invalid history builder", 3, diff.size());
	}

	@Test
	public void testPatch() {

		Building building = buildingService.readFull(TestData.IVANOVA_2);
		if (building == null) {
			throw new IllegalStateException("No building ivanova-2 found");
		}

		Diff diff = historyBuilder.diff(null, building);

		Building newBuilding = factory.newBuilding();
		historyBuilder.patch(newBuilding, diff);

		assertEquals("Invalid district reference patch", building.getDistrictStub(), newBuilding.getDistrictStub());
		for (BuildingAddress address : newBuilding.getBuildingses()) {
			BuildingAddress oldAddress = building.getAddressOnStreet(address.getStreetStub());
			assertNotNull("Address sync failed", oldAddress);
			assertEquals("Invalid primary status patch", oldAddress.isPrimary(), address.isPrimary());
		}

		building.setPrimaryAddress(TestData.ADDR_ROSSIISKAYA_220R);
		diff = historyBuilder.diff(newBuilding, building);
		historyBuilder.patch(newBuilding, diff);

		BuildingAddress oldAddress = building.getDefaultBuildings();
		BuildingAddress newAddress = newBuilding.getDefaultBuildings();
		assertNotNull("Old address not found", oldAddress);
		assertNotNull("New address not found", newAddress);
		assertEquals("Invalid primary status patch", oldAddress.getStreetStub(), newAddress.getStreetStub());

		BuildingAddress toDelete = building.getAddressOnStreet(TestData.ROSSIISKAYA);
		assertNotNull("No address found on ROSSIISKAYA", toDelete);
		toDelete.disable();
		diff = historyBuilder.diff(newBuilding, building);
		historyBuilder.patch(newBuilding, diff);
		int addressCount = 0;
		for (BuildingAddress address : newBuilding.getBuildingses()) {
			if (address.isActive()) {
				++addressCount;
			}
		}
		assertEquals("Address delete patch failed", 2, addressCount);
	}
}
