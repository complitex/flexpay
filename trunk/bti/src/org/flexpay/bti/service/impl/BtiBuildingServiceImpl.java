package org.flexpay.bti.service.impl;

import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.bti.dao.BtiBuildingDaoExt;
import org.flexpay.bti.persistence.BtiBuilding;
import org.flexpay.bti.service.BtiBuildingService;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class BtiBuildingServiceImpl implements BtiBuildingService {

	private BuildingDao buildingDao;
	private BtiBuildingDaoExt btiBuildingDaoExt;

	/**
	 * Read bti building with associated attributes
	 *
	 * @param stub building stub to read
	 * @return Building if found, or <code>null</code> otherwise
	 */
	public BtiBuilding readWithAttributes(Stub<BtiBuilding> stub) {
		return btiBuildingDaoExt.readBuildinWithAttributes(stub.getId());
	}

	/**
	 * Read bti building with associated attributes
	 *
	 * @param stub building stub to read
	 * @return Building if found, or <code>null</code> otherwise
	 */
	public BtiBuilding readWithAttributesByAddress(Stub<BuildingAddress> stub) {
		return btiBuildingDaoExt.readBuildinWithAttributesByAddress(stub.getId());
	}

	/**
	 * Update building attributes
	 *
	 * @param building Building to update
	 * @return building back
	 */
	@Transactional (readOnly = false)
	public BtiBuilding updateAttributes(@NotNull BtiBuilding building) {
		buildingDao.update(building);

		return building;
	}

	@Required
	public void setBtiBuildingDaoExt(BtiBuildingDaoExt btiBuildingDaoExt) {
		this.btiBuildingDaoExt = btiBuildingDaoExt;
	}

	@Required
	public void setBuildingDao(BuildingDao buildingDao) {
		this.buildingDao = buildingDao;
	}
}
