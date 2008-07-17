package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.Buildings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface BuildingsDaoExt {

	/**
	 * Find building by number
	 *
	 * @param streetId   Street key
	 * @param districtId District key
	 * @param number	 Building number
	 * @return Buildingses list
	 */
	@NotNull
	List<Buildings> findBuildings(@NotNull Long streetId, @NotNull Long districtId, @NotNull String number);

	/**
	 * Find building by number
	 *
	 * @param streetId Street key
	 * @param number   Building number
	 * @return Buildingses list
	 */
	@NotNull
	List<Buildings> findBuildings(@NotNull Long streetId, @NotNull String number);

	/**
	 * Find Building stub by Buildings stub (i.e. object that does not have reference to its building)
	 *
	 * @param buildings Buildings stub
	 * @return Building if found, or <code>null</code> otherwise
	 */
	@Nullable
	Building findBuilding(@NotNull Long buildings);
}
