package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;

/**
 * Persistence objects factory. Used to handle module dependent objects creation as all module object should be be of
 * the same type
 */
public interface ObjectsFactory {

	/**
	 * Create a new Apartment
	 *
	 * @return new apartment instance
	 */
	Apartment newApartment();

	/**
	 * Create a new Building
	 *
	 * @return new building instance
	 */
	Building newBuilding();
}
