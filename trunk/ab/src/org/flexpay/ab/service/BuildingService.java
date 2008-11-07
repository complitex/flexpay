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
	 * Get building attribute types
	 *
	 * @return BuildingAttributeType list
	 */
	public List<BuildingAttributeType> getAttributeTypes();

	/**
	 * Find buildings by attributes
	 *
	 * @param street	 Building street stub
	 * @param district   Building district stub
	 * @param attributes Building attributes
	 * @return Buildings instance, or <code>null</null> if not found
	 * @throws FlexPayException if failure occurs
	 */
	@Nullable
	Buildings findBuildings(@NotNull Stub<Street> street, @Nullable Stub<District> district,
							@NotNull Set<BuildingAttribute> attributes)
			throws FlexPayException;

	/**
	 * Find building by number
	 *
	 * @param street   Building street stub
	 * @param district Building district stub
	 * @param number   Building number
	 * @param bulk	 Building bulk number
	 * @return Buildings instance, or <code>null</null> if not found
	 * @throws FlexPayException if failure occurs
	 * @deprecated use {@link #findBuildings(org.flexpay.common.persistence.Stub, org.flexpay.common.persistence.Stub,
	 *			 java.util.Set)}
	 */
	@Nullable
	Buildings findBuildings(@NotNull Stub<Street> street, @NotNull Stub<District> district,
							String number, String bulk) throws FlexPayException;

	/**
	 * Find building by number
	 *
	 * @param streetStub Street stub
	 * @param number	 Building number
	 * @param bulk	   Building bulk number
	 * @return Buildings instance, or <code>null</null> if not found
	 * @throws FlexPayException if failure occurs
	 * @deprecated use {@link #findBuildings(org.flexpay.common.persistence.Stub, org.flexpay.common.persistence.Stub,
	 *			 java.util.Set)}
	 */
	@Nullable
	Buildings findBuildings(@NotNull Stub<Street> streetStub, String number, String bulk) throws FlexPayException;

	/**
	 * Find building by buildings stub
	 *
	 * @param stub Buildings stub
	 * @return Building instance if buildings found, or <code>null</code> otherwise
	 */
	@Nullable
	Building findBuilding(@NotNull Stub<Buildings> stub);

	/**
	 * Find single Building relation for building stub
	 *
	 * @param stub Building stub
	 * @return Buildings instance
	 * @throws FlexPayException if building does not have any buildingses
	 */
	Buildings getFirstBuildings(Stub<Building> stub) throws FlexPayException;

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
	 * @param stub Building stub
	 * @return List of Buildings
	 * @throws FlexPayException if building does not have any buildingses
	 */
	List<Buildings> getBuildingBuildings(Stub<Building> stub)
			throws FlexPayException;

	Building readBuilding(Long id);

	BuildingAttributeType createBuildingAttributeType(BuildingAttributeType type);

	void updateBuildingAttributeType(BuildingAttributeType type);

	/**
	 * Get building attribute type
	 *
	 * @param stub BuildingAttributeType stub
	 * @return Attribute type if found, or <code>null</code> otherwise
	 */
	@Nullable
	BuildingAttributeType getAttributeType(@NotNull Stub<BuildingAttributeType> stub);
}
