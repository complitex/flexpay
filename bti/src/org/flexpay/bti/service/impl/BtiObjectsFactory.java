package org.flexpay.bti.service.impl;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.bti.persistence.building.BtiBuilding;

public class BtiObjectsFactory implements ObjectsFactory {

	/**
	 * Create a new Apartment
	 *
	 * @return new apartment instance
	 */
	public Apartment newApartment() {
		return BtiApartment.newInstance();
	}

	/**
	 * Create a new Building
	 *
	 * @return new building instance
	 */
	public Building newBuilding() {
		return BtiBuilding.newInstance();
	}
}
