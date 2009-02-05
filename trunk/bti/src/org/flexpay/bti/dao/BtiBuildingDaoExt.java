package org.flexpay.bti.dao;

import org.flexpay.bti.persistence.BtiBuilding;
import org.flexpay.ab.persistence.Town;
import org.flexpay.common.persistence.Stub;

import java.util.List;

public interface BtiBuildingDaoExt {

	BtiBuilding readBuildinWithAttributes(Long buildingId);

	BtiBuilding readBuildinWithAttributesByAddress(Long addressId);

	List<BtiBuilding> findByTown(Stub<Town> town);
}
