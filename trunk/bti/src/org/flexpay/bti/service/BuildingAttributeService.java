package org.flexpay.bti.service;

import org.flexpay.bti.persistence.BuildingTempAttribute;
import org.flexpay.bti.persistence.BtiBuilding;
import org.flexpay.bti.persistence.BuildingAttribute;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BuildingAttributeService {

	/**
	 * Find temp attributes of a building
	 *
	 * @param stub Building stub
	 * @param pager Page
	 * @return list of building attributes
	 */
	List<BuildingAttribute> listAttributes(@NotNull Stub<BtiBuilding> stub, Page<BuildingAttribute> pager);
}
