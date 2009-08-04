package org.flexpay.bti.service.impl;

import org.flexpay.bti.dao.BtiBuildingDaoExt;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.bti.persistence.building.BuildingAttribute;
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
	public List<BuildingAttribute> listAttributes(@NotNull Stub<BtiBuilding> stub, Page<BuildingAttribute> pager) {
		List<BuildingAttribute> attributes = listAttributes(stub);
		pager.setTotalElements(attributes.size());
		return CollectionUtils.listSlice(attributes,
				pager.getThisPageFirstElementNumber(), pager.getThisPageLastElementNumber());
	}

	public List<BuildingAttribute> listAttributes(@NotNull Stub<BtiBuilding> stub) {
		BtiBuilding building = buildingDaoExt.readBuildingWithAttributes(stub.getId());
		return CollectionUtils.list(building.currentAttributes());
	}

	@Required
	public void setBuildingDaoExt(BtiBuildingDaoExt buildingDaoExt) {
		this.buildingDaoExt = buildingDaoExt;
	}

}
