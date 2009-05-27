package org.flexpay.ab.service.imp;

import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;

public class AbObjectsFactory implements ObjectsFactory {

	/**
	 * Create a new Apartment
	 *
	 * @return new apartment instance
	 */
	public Apartment newApartment() {
		return Apartment.newInstance();
	}

	/**
	 * Create a new Building
	 *
	 * @return new building instance
	 */
	public Building newBuilding() {
		return Building.newInstance();
	}
}
