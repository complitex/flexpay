package org.flexpay.bti.service.impl;

import org.flexpay.bti.service.BuildingAttributeService;
import org.flexpay.bti.persistence.BtiBuilding;
import org.flexpay.bti.persistence.BuildingAttribute;
import org.flexpay.bti.dao.BuildingAttributeDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class BuildingAttributeServiceImpl implements BuildingAttributeService {

	private BuildingAttributeDao attributeDao;

	/**
	 * Find temp attributes of a building
	 *
	 * @param stub Building stub
	 * @param pager Page
	 * @return list of building attributes
	 */
	public List<BuildingAttribute> listAttributes(@NotNull Stub<BtiBuilding> stub, Page<BuildingAttribute> pager) {
		return attributeDao.findAttributes(stub.getId(), pager);
	}

	public void setAttributeDao(BuildingAttributeDao attributeDao) {
		this.attributeDao = attributeDao;
	}
}
