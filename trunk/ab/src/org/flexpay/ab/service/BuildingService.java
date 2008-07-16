package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.ParentService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface BuildingService extends ParentService<BuildingsFilter> {

	public List<Buildings> getBuildings(ArrayStack filters, Page pager);

	public List<Buildings> getBuildings(Long streetId, Page pager);

	/**
	 * Get building attribute type
	 *
	 * @param type Attribute type
	 * @return BuildingAttributeType
	 * @throws FlexPayException if failure occurs
	 */
	public BuildingAttributeType getAttributeType(int type)
			throws FlexPayException;

	/**
	 * Get building attribute types
	 *
	 * @return BuildingAttributeType list
	 */
	public List<BuildingAttributeType> getAttributeTypes();

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
	 * Find building by buildings stub
	 *
	 * @param buildingsStub object with id only
	 * @return Building instance
	 */
	Building findBuilding(Buildings buildingsStub);

	/**
	 * Find single Building relation for building stub
	 *
	 * @param building Building stub
	 * @return Buildings instance
	 * @throws FlexPayException if building does not have any buildingses
	 */
	Buildings getFirstBuildings(Building building) throws FlexPayException;

	/**
	 * Read full buildings info
	 *
	 * @param stub Buildins stub
	 * @return Buildings if found, or <code>null</code> othrwise
	 */
	@Nullable
	Buildings readFull(@NotNull Stub<Buildings> stub);

	/**
	 * Update buildings
	 *
	 * @param buildings Buildings
	 */
	void update(Buildings buildings);

	/**
	 * Create a new Buildings
	 *
	 * @param street   Street
	 * @param district District
	 * @param attrs	Buildings attributes
	 * @return new Buildings object created
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	Buildings createStreetDistrictBuildings(@NotNull Stub<Street> street, @NotNull Stub<District> district,
							  @NotNull Set<BuildingAttribute> attrs)
			throws FlexPayException;

	/**
	 * Create a new Buildings
	 *
	 * @param building Building
	 * @param street   Street
	 * @param attrs	Buildings attributes
	 * @return new Buildings object created
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	Buildings createStreetBuildings(@NotNull Stub<Building> building, @NotNull Stub<Street> street,
									@NotNull Set<BuildingAttribute> attrs)
			throws FlexPayException;

	/**
	 * Find all Buildings relation for building stub
	 *
	 * @param building Building stub
	 * @return List of Buildings
	 * @throws FlexPayException if building does not have any buildingses
	 */
	List<Buildings> getBuildingBuildings(Building building)
			throws FlexPayException;

	Building readBuilding(Long id);

	BuildingAttributeType createBuildingAttributeType(BuildingAttributeType type);

	void updateBuildingAttributeType(BuildingAttributeType type);
}
