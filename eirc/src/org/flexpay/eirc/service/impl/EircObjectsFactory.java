package org.flexpay.eirc.service.impl;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.eirc.persistence.ServedBuilding;

public class EircObjectsFactory implements ObjectsFactory {

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
		return ServedBuilding.newInstance();
	}
}
