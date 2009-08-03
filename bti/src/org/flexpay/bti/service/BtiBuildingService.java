package org.flexpay.bti.service;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.Building;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface BtiBuildingService {

	/**
	 * Read bti building with associated attributes
	 *
	 * @param stub building stub to read
	 * @return Building if found, or <code>null</code> otherwise
	 */
	@Nullable
	BtiBuilding readWithAttributes(Stub<? extends Building> stub);

	/**
	 * Read bti building with associated attributes
	 *
	 * @param stub building stub to read
	 * @return Building if found, or <code>null</code> otherwise
	 */
	@Nullable
	BtiBuilding readWithAttributesByAddress(Stub<BuildingAddress> stub);

	/**
	 * Update building attributes
	 *
	 * @param building Building to update
	 * @return building back
	 */
	BtiBuilding updateAttributes(@NotNull BtiBuilding building);

	/**
	 * Find all BtiBuilding in the town
	 * @param town town to search
	 * @return BtiBuilding list in town
	 */
	List<BtiBuilding> findByTown(Stub<Town> town);

}
