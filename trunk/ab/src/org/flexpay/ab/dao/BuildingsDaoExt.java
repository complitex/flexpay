package org.flexpay.ab.dao;

import java.util.Set;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAttribute;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Street;

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
	Buildings findBuildings(Street street, String number, String bulk);
	
	/**
	 * Find building by street and attributes
	 *
	 * @param street Building street
	 * @param buildingAttributes Building attributes
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	Buildings findBuildings(Street street, Set<BuildingAttribute> buildingAttributes);

	/**
	 * Find Building stub by Buildings stub (i.e. object that does not have reference to its building)
	 * @param buildings Buildings stub
	 * @return Building if found, or <code>null</code> otherwise
	 */
	Building findBuilding(Buildings buildings);
}
