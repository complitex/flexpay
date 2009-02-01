package org.flexpay.bti.dao;

import org.flexpay.bti.persistence.BtiBuilding;

public interface BtiBuildingDaoExt {

	BtiBuilding readBuildinWithAttributes(Long buildingId);

	BtiBuilding readBuildinWithAttributesByAddress(Long addressId);
}
