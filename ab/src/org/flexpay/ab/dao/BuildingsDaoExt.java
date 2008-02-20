package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.District;

public interface BuildingsDaoExt {

	/**
	 * Find building by number
	 *
	 * @param street   Building street
	 * @param district Building district
	 * @param number   Building number
	 * @param bulk	 Building bulk number
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	public Buildings findBuildings(Street street, District district, String number, String bulk);	
}
