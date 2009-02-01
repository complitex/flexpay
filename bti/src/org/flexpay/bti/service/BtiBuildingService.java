package org.flexpay.bti.service;

import com.sun.istack.internal.Nullable;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.bti.persistence.BtiBuilding;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

public interface BtiBuildingService {

	/**
	 * Read bti building with associated attributes
	 *
	 * @param stub building stub to read
	 * @return Building if found, or <code>null</code> otherwise
	 */
	@Nullable
	BtiBuilding readWithAttributes(Stub<BtiBuilding> stub);

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
}
