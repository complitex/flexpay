package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.List;
import java.util.Set;
import java.util.Locale;

public interface BuildingService extends ParentService<BuildingsFilter> {

	/**
	 * Initialize parent filter. Possibly taking in account upper level forefather filter
	 *
	 * @param parentFilter	 Filter to init
	 * @param forefatherFilter Upper level filter
	 * @param locale		   Locale to get parent names in
	 * @return Initialised filter
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	@Secured (Roles.BUILDING_READ)
	BuildingsFilter initFilter(BuildingsFilter parentFilter, PrimaryKeyFilter forefatherFilter, Locale locale)
			throws FlexPayException;

	/**
	 * Initialize filters. <p>Filters are coming from the most significant to less significant ones order, like
	 * CountryFilter, RegionFilter, TownFilter for example</p>
	 *
	 * @param filters Filters to init
	 * @param locale  Locale to get parent names in
	 * @return Initialised filters stack
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	@Secured (Roles.BUILDING_READ)
	ArrayStack initFilters(ArrayStack filters, Locale locale) throws FlexPayException;

	@Secured (Roles.BUILDING_READ)
	List<Buildings> getBuildings(ArrayStack filters, Page pager);

	/**
	 * @param streetId
	 * @param pager
	 * @return
	 * @deprecated use {@link #getBuildings(org.apache.commons.collections.ArrayStack, org.flexpay.common.dao.paging.Page)}
	 *             instead
	 */
	@Secured (Roles.BUILDING_READ)
	List<Buildings> getBuildings(Long streetId, Page pager);

	/**
	 * Get building attribute types
	 *
	 * @return BuildingAttributeType list
	 */
	@Secured (Roles.BUILDING_ATTRIBUTE_TYPE_READ)
	List<BuildingAttributeType> getAttributeTypes();

	/**
	 * Find buildings by attributes
	 *
	 * @param street	 Building street stub
	 * @param district   Building district stub
	 * @param attributes Building attributes
	 * @return Buildings instance, or <code>null</null> if not found
	 * @throws FlexPayException if failure occurs
	 */
	@Secured (Roles.BUILDING_READ)
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
	@Secured (Roles.BUILDING_READ)
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
	@Secured (Roles.BUILDING_READ)
	@Nullable
	Buildings findBuildings(@NotNull Stub<Street> streetStub, String number, String bulk) throws FlexPayException;

	/**
	 * Find building by buildings stub
	 *
	 * @param stub Buildings stub
	 * @return Building instance if buildings found, or <code>null</code> otherwise
	 */
	@Secured (Roles.BUILDING_READ)
	@Nullable
	Building findBuilding(@NotNull Stub<Buildings> stub);

	/**
	 * Find single Building relation for building stub
	 *
	 * @param stub Building stub
	 * @return Buildings instance
	 * @throws FlexPayException if building does not have any buildingses
	 */
	@Secured (Roles.BUILDING_READ)
	Buildings getFirstBuildings(Stub<Building> stub) throws FlexPayException;

	/**
	 * Read full buildings info
	 *
	 * @param stub Buildins stub
	 * @return Buildings if found, or <code>null</code> othrwise
	 */
	@Secured (Roles.BUILDING_READ)
	@Nullable
	Buildings readFull(@NotNull Stub<Buildings> stub);

	/**
	 * Update buildings
	 *
	 * @param buildings Buildings
	 */
	@Secured (Roles.BUILDING_CHANGE)
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
	@Secured (Roles.BUILDING_ADD)
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
	@Secured (Roles.BUILDING_ADD)
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
	@Secured (Roles.BUILDING_READ)
	List<Buildings> getBuildingBuildings(Stub<Building> stub)
			throws FlexPayException;

	/**
	 *
	 * @param id
	 * @return
	 * @deprecated refactor me
	 */
	@Secured (Roles.BUILDING_READ)
	Building readBuilding(Long id);

	/**
	 * Get building attribute type
	 *
	 * @param stub BuildingAttributeType stub
	 * @return Attribute type if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.BUILDING_READ)
	@Nullable
	BuildingAttributeType read(@NotNull Stub<BuildingAttributeType> stub);

	/**
	 * Create or update building attribute type
	 *
	 * @param type AttributeType to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured ({Roles.BUILDING_ATTRIBUTE_TYPE_ADD, Roles.BUILDING_ATTRIBUTE_TYPE_CHANGE})
	void save(@NotNull BuildingAttributeType type) throws FlexPayExceptionContainer;

}
