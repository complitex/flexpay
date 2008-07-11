package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

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
	 * @param streetId		   Building street key
	 * @param buildingAttributes Building attributes
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	public Buildings findBuildings(@NotNull Long streetId, @NotNull Set<BuildingAttribute> buildingAttributes);

	/**
	 * Find Building stub by Buildings stub (i.e. object that does not have reference to its building)
	 *
	 * @param buildings Buildings stub
	 * @return Building if found, or <code>null</code> otherwise
	 */
	Building findBuilding(Buildings buildings);
}
