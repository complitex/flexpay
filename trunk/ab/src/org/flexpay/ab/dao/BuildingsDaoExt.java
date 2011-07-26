package org.flexpay.ab.dao;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.common.dao.JpaSetDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface BuildingsDaoExt extends JpaSetDao {

	/**
	 * Find building by number
	 *
	 * @param streetId   Street key
	 * @param districtId District key
	 * @param number	 Building number
	 * @return Buildingses list
	 */
	@NotNull
	List<BuildingAddress> findBuildings(@NotNull Long streetId, @NotNull Long districtId, @NotNull String number);

	/**
	 * Find building by number
	 *
	 * @param streetId Street key
	 * @param number   Building number
	 * @return Buildingses list
	 */
	@NotNull
	List<BuildingAddress> findBuildings(@NotNull Long streetId, @NotNull String number);

	/**
	 * Find Building stub by Buildings stub (i.e. object that does not have reference to its building)
	 *
	 * @param buildings Buildings stub
	 * @return Building if found, or <code>null</code> otherwise
	 */
	@Nullable
	Building findBuilding(@NotNull Long buildings);

	/**
	 * Find and sort buildings
	 *
	 * @param filters Building filters
	 * @param sorters Collection of sorters
	 * @param pager   Pager
	 * @return List of addresses
	 */
	@NotNull
	List<BuildingAddress> findBuildingAddresses(ArrayStack filters, Collection<? extends ObjectSorter> sorters, Page<BuildingAddress> pager);

}
