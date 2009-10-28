package org.flexpay.ab.service.impl;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.ObjectsFactory;

public class AbObjectsFactory implements ObjectsFactory {

	/**
	 * Create a new Apartment
	 *
	 * @return new apartment instance
	 */
	@Override
	public Apartment newApartment() {
		return Apartment.newInstance();
	}

	/**
	 * Create a new Building
	 *
	 * @return new building instance
	 */
	@Override
	public Building newBuilding() {
		return Building.newInstance();
	}

}
