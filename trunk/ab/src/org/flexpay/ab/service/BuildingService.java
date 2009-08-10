package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.ParentService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
	List<BuildingAddress> getBuildings(ArrayStack filters, Page<BuildingAddress> pager);

	@Secured (Roles.BUILDING_READ)
	@NotNull
	List<BuildingAddress> getBuildings(ArrayStack filters, List<? extends ObjectSorter> sorters, Page<BuildingAddress> pager);

	@Secured (Roles.BUILDING_READ)
	List<BuildingAddress> getBuildings(@NotNull Stub<Street> stub, Page<BuildingAddress> pager);

	@Secured (Roles.BUILDING_READ)
	List<BuildingAddress> getBuildings(@NotNull Stub<Street> stub);

	/**
	 * Find buildings by street and attributes
	 *
	 * @param street	 Building street stub
	 * @param attributes Building attributes
	 * @return Buildings instance, or <code>null</null> if not found
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	BuildingAddress findBuildings(@NotNull Stub<Street> street, @NotNull Set<AddressAttribute> attributes) throws FlexPayException;

	Set<AddressAttribute> attributes(@NotNull String number, @Nullable String bulk);

	/**
	 * Find buildings by street, district and attributes
	 *
	 * @param street	 Building street stub
	 * @param district   Building district stub
	 * @param attributes Building attributes
	 * @return Buildings instance, or <code>null</null> if not found
	 * @throws FlexPayException if failure occurs
	 */
	@Secured (Roles.BUILDING_READ)
	@Nullable
	BuildingAddress findBuildings(@NotNull Stub<Street> street, @Nullable Stub<District> district,
								  @NotNull Set<AddressAttribute> attributes)
			throws FlexPayException;

	/**
	 * Find building by buildings stub
	 *
	 * @param stub Buildings stub
	 * @return Building instance if buildings found, or <code>null</code> otherwise
	 */
	@Secured (Roles.BUILDING_READ)
	@Nullable
	Building findBuilding(@NotNull Stub<BuildingAddress> stub);

	/**
	 * Find single Building relation for building stub
	 *
	 * @param stub Building stub
	 * @return Buildings instance
	 * @throws FlexPayException if building does not have any buildingses
	 */
	@Secured (Roles.BUILDING_READ)
	BuildingAddress getFirstBuildings(Stub<Building> stub) throws FlexPayException;

	/**
	 * Read full buildings info
	 *
	 * @param stub Buildings stub
	 * @return Buildings if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.BUILDING_READ)
	@Nullable
	BuildingAddress readFull(@NotNull Stub<BuildingAddress> stub);

	/**
	 * Read full buildings info
	 *
	 * @param ids Buildings keys
	 * @param preserveOrder Whether to preserve order of buildings
	 * @return Buildings found
	 */
	@Secured (Roles.BUILDING_READ)
	@NotNull
	List<Building> readFull(Collection<Long> ids, boolean preserveOrder);

	/**
	 * Find all Buildings relation for building stub
	 *
	 * @param stub Building stub
	 * @return List of Buildings
	 * @throws FlexPayException if building does not have any buildingses
	 */
	@Secured (Roles.BUILDING_READ)
	List<BuildingAddress> getBuildingBuildings(Stub<Building> stub) throws FlexPayException;

	/**
	 * Find all buildings on a specified street
	 *
	 * @param stub Street stub
	 * @return List of buildings on a street
	 */
	@NotNull
	List<Building> findStreetBuildings(Stub<Street> stub);

	/**
	 * Read building info
	 *
	 * @param stub Building stub
	 * @return Building if found
	 */
	@Secured (Roles.BUILDING_READ)
	Building read(@NotNull Stub<Building> stub);

	/**
	 * Update building
	 *
	 * @param building Building to update
	 * @return updated building back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.BUILDING_CHANGE)
	@NotNull
	Building update(@NotNull Building building) throws FlexPayExceptionContainer;

	/**
	 * Create building
	 *
	 * @param building Building to create
	 * @return persisted building back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.BUILDING_ADD)
	@NotNull
	Building create(@NotNull Building building) throws FlexPayExceptionContainer;

	/**
	 * Disable buildings
	 *
	 * @param buildings Buildings to disable
	 */
	@Secured (Roles.BUILDING_DELETE)
	void disable(Collection<Building> buildings);

	/**
	 * Read full address info by their ids
	 *
	 * @param addressIds Address ids
	 * @param preserveOrder Whether to preserve elements order
	 * @return List of buildings
	 */
	List<BuildingAddress> readFullAddresses(Collection<Long> addressIds, boolean preserveOrder);
}
