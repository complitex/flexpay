package org.flexpay.bti.service;

import org.flexpay.bti.persistence.BtiBuilding;
import org.flexpay.bti.persistence.BuildingAttributeBase;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BuildingAttributeService {

	/**
	 * Find temp attributes of a building
	 *
	 * @param stub  Building stub
	 * @param pager Page
	 * @return list of building attributes
	 */
	List<BuildingAttributeBase> listAttributes(@NotNull Stub<BtiBuilding> stub, Page<BuildingAttributeBase> pager);
}
