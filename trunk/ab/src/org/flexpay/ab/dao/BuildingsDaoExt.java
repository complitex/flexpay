package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Building;

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
	Buildings findBuildings(Street street, District district, String number, String bulk);

	/**
	 * Find building by number
	 *
	 * @param street Building street
	 * @param number Building number
	 * @param bulk   Building bulk number
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	public Buildings findBuildings(Street street, String number, String bulk);

	/**
	 * Find Building stub by Buildings stub (i.e. object that does not have reference to its building)
	 * @param buildings Buildings stub
	 * @return Building if found, or <code>null</code> otherwise
	 */
	Building findBuilding(Buildings buildings);
}
