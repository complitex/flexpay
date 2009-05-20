package org.flexpay.bti.service;

import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.bti.persistence.building.BuildingAttributeBase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BuildingAttributeService {

	/**
	 * Find attributes of a building
	 *
	 * @param stub  Building stub
	 * @param pager Page
	 * @return list of building attributes
	 */
	List<BuildingAttributeBase> listAttributes(@NotNull Stub<BtiBuilding> stub, Page<BuildingAttributeBase> pager);

    List<BuildingAttributeBase> listAttributes(@NotNull Stub<BtiBuilding> stub);

}
