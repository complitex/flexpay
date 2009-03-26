package org.flexpay.bti.service.impl;

import org.flexpay.bti.dao.BtiBuildingDaoExt;
import org.flexpay.bti.persistence.BtiBuilding;
import org.flexpay.bti.persistence.BuildingAttributeBase;
import org.flexpay.bti.service.BuildingAttributeService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class BuildingAttributeServiceImpl implements BuildingAttributeService {

	private BtiBuildingDaoExt buildingDaoExt;

	/**
	 * Find attributes of a building
	 *
	 * @param stub  Building stub
	 * @param pager Page
	 * @return list of building attributes
	 */
	public List<BuildingAttributeBase> listAttributes(@NotNull Stub<BtiBuilding> stub, Page<BuildingAttributeBase> pager) {
		List<BuildingAttributeBase> attributes = listAttributes(stub);
		pager.setTotalElements(attributes.size());
		return CollectionUtils.listSlice(attributes,
				pager.getThisPageFirstElementNumber(), pager.getThisPageLastElementNumber());
	}

	public List<BuildingAttributeBase> listAttributes(@NotNull Stub<BtiBuilding> stub) {
		BtiBuilding building = buildingDaoExt.readBuildinWithAttributes(stub.getId());
		return CollectionUtils.list(building.getAttributes());
	}

	@Required
	public void setBuildingDaoExt(BtiBuildingDaoExt buildingDaoExt) {
		this.buildingDaoExt = buildingDaoExt;
	}

}
