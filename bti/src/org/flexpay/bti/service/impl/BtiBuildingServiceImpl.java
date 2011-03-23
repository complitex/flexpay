package org.flexpay.bti.service.impl;

import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Town;
import org.flexpay.bti.dao.BtiBuildingDaoExt;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.bti.service.BtiBuildingService;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

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
	public BtiBuilding readWithAttributes(Stub<? extends Building> stub) {
		return btiBuildingDaoExt.readBuildingWithAttributes(stub.getId());
	}

	/**
	 * Read bti buildings with associated attributes
	 *
	 * @param ids buildings identifiers to read
	 * @return Buildings list
	 */
	@NotNull
	@Override
	public List<BtiBuilding> readWithAttributes(Collection<Long> ids) {
		return btiBuildingDaoExt.readBuildingWithAttributes(ids);
	}

	/**
	 * Read bti building with associated attributes
	 *
	 * @param stub building stub to read
	 * @return Building if found, or <code>null</code> otherwise
	 */
	public BtiBuilding readWithAttributesByAddress(Stub<BuildingAddress> stub) {
		return btiBuildingDaoExt.readBuildingWithAttributesByAddress(stub.getId());
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

	/**
	 * Find all BtiBuilding in the town
	 *
	 * @param town town to search
	 * @return BtiBuilding list in town
	 */
	public List<BtiBuilding> findByTown(Stub<Town> town) {
		return btiBuildingDaoExt.findByTown(town);
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
