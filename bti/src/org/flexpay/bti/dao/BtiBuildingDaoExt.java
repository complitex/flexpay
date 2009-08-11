package org.flexpay.bti.dao;

import org.flexpay.ab.persistence.Town;
import org.flexpay.bti.persistence.building.BtiBuilding;
import org.flexpay.common.persistence.Stub;

import java.util.List;
import java.util.Collection;

public interface BtiBuildingDaoExt {

	BtiBuilding readBuildingWithAttributes(Long buildingId);

	BtiBuilding readBuildingWithAttributesByAddress(Long addressId);

	List<BtiBuilding> findByTown(Stub<Town> town);

	List<BtiBuilding> readBuildingWithAttributes(Collection<Long> ids);
}
