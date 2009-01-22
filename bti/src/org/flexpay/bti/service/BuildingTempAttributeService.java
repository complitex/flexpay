package org.flexpay.bti.service;

import org.flexpay.bti.persistence.BuildingTempAttribute;
import org.flexpay.bti.persistence.BtiBuilding;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BuildingTempAttributeService {

	/**
	 * Find temp attributes of a building
	 *
	 * @param stub Building stub
	 * @param pager
	 * @return list of building attributes
	 */
	List<BuildingTempAttribute> listAttributes(@NotNull Stub<BtiBuilding> stub, Page<BuildingTempAttribute> pager);
}
