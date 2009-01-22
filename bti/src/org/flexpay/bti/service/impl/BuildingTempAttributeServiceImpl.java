package org.flexpay.bti.service.impl;

import org.flexpay.bti.service.BuildingTempAttributeService;
import org.flexpay.bti.persistence.BuildingTempAttribute;
import org.flexpay.bti.persistence.BtiBuilding;
import org.flexpay.bti.dao.BuildingTempAttributeDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BuildingTempAttributeServiceImpl implements BuildingTempAttributeService {

	private BuildingTempAttributeDao attributeDao;

	/**
	 * Find temp attributes of a building
	 *
	 * @param stub Building stub
	 * @param pager Page
	 * @return list of building attributes
	 */
	public List<BuildingTempAttribute> listAttributes(@NotNull Stub<BtiBuilding> stub, Page<BuildingTempAttribute> pager) {
		return attributeDao.findAttributes(stub.getId(), pager);
	}

	public void setAttributeDao(BuildingTempAttributeDao attributeDao) {
		this.attributeDao = attributeDao;
	}
}
