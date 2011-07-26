package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.JpaSetService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface BuildingService extends JpaSetService {

	/**
	 * Read building
	 *
	 * @param buildingStub Building stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.BUILDING_READ)
	@Nullable
	Building readFull(@NotNull Stub<Building> buildingStub);

	/**
	 * Read buildings collection by theirs ids
	 *
 	 * @param buildingIds Building ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found buildings
	 */
	@Secured (Roles.BUILDING_READ)
	@NotNull
	List<Building> readFull(@NotNull Collection<Long> buildingIds, boolean preserveOrder);

	/**
	 * Disable buildings
	 *
	 * @param buildingIds IDs of buildings to disable
	 */
	@Secured (Roles.BUILDING_DELETE)
	void disable(@NotNull Collection<Long> buildingIds);

	/**
	 * Create building
	 *
	 * @param building Building to save
	 * @return Saved instance of building
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.BUILDING_ADD)
	@NotNull
	Building create(@NotNull Building building) throws FlexPayExceptionContainer;

	/**
	 * Update or create building
	 *
	 * @param building Building to save
	 * @return Saved instance of building
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@Secured (Roles.BUILDING_CHANGE)
	@NotNull
	Building update(@NotNull Building building) throws FlexPayExceptionContainer;

	/**
	 * Find building by building address stub
	 *
	 * @param addressStub BuildingAddress stub
	 * @return Building instance if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.BUILDING_READ)
	@Nullable
	Building findBuilding(@NotNull Stub<BuildingAddress> addressStub);

	/**
	 * Read building address
	 *
	 * @param addressStub BuildingAddress stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Secured (Roles.BUILDING_READ)
	@Nullable
	BuildingAddress readFullAddress(@NotNull Stub<BuildingAddress> addressStub);

	/**
	 * Read building address with its full hierarchical structure:
	 * country-region-town-street
	 *
	 * @param addressStub Building address stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Secured ({Roles.BUILDING_READ})
	@Nullable
	BuildingAddress readWithHierarchy(@NotNull Stub<BuildingAddress> addressStub);

	/**
	 * Read building addresses collection by theirs ids
	 *
 	 * @param addressIds BuildingAddress ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found building addresses
	 */
	@Secured (Roles.BUILDING_READ)
	@NotNull
	List<BuildingAddress> readFullAddresses(Collection<Long> addressIds, boolean preserveOrder);

	/**
	 * Disable building addresses
	 *
	 * @param addressIds IDs of building addresses to disable
	 * @param buildingStub Addresses building stub
	 */
	@Secured (Roles.BUILDING_DELETE)
	void disableAddresses(@NotNull Collection<Long> addressIds, @NotNull Stub<Building> buildingStub);

	/**
	 * Get a list of available building addresses
	 *
	 * @param filters Parent filters
	 * @param sorters Stack of sorters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@Secured (Roles.BUILDING_READ)
	@NotNull
	List<BuildingAddress> findAddresses(@NotNull ArrayStack filters, @NotNull List<? extends ObjectSorter> sorters, @NotNull Page<BuildingAddress> pager);

	/**
	 * Get a list of available building addresses
	 *
	 * @param filters Parent filters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@Secured (Roles.BUILDING_READ)
	@NotNull
	List<BuildingAddress> findAddresses(@NotNull ArrayStack filters, Page<BuildingAddress> pager);

	/**
	 * Lookup building address by street id.
	 *
	 * @param streetStub  Street stub
	 * @return List of found building addresses
	 */
	@Secured (Roles.BUILDING_READ)
	@NotNull
	List<BuildingAddress> findAddressesByParent(@NotNull Stub<Street> streetStub);

	/**
	 * Find building addresses for building
	 *
	 * @param buildingStub Building stub
	 * @return List of building addresses
	 * @throws FlexPayException if building does not have any buildingses
	 */
	@Secured (Roles.BUILDING_READ)
	@NotNull
	List<BuildingAddress> findAddresesByBuilding(Stub<Building> buildingStub) throws FlexPayException;

	/**
	 * Find building addresses by street and attributes
	 *
	 * @param streetStub Building street stub
	 * @param attributes Building attributes
	 * @return BuildingAddress instance, or <code>null</null> if not found
	 * @throws FlexPayException if failure occurs
	 */
	@Secured (Roles.BUILDING_READ)
	@Nullable
	BuildingAddress findAddresses(@NotNull Stub<Street> streetStub, @NotNull Set<AddressAttribute> attributes) throws FlexPayException;

	/**
	 * Find building addresses by street, district and attributes
	 *
	 * @param streetStub Building street stub
	 * @param districtStub Building district stub
	 * @param attributes Building attributes
	 * @return BuildingAddress instance, or <code>null</null> if not found
	 * @throws FlexPayException if failure occurs
	 */
	@Secured (Roles.BUILDING_READ)
	@Nullable
	BuildingAddress findAddresses(@NotNull Stub<Street> streetStub, @Nullable Stub<District> districtStub,
								  @NotNull Set<AddressAttribute> attributes) throws FlexPayException;

	/**
	 * Find single Building relation for building stub
	 *
	 * @param buildingStub Building stub
	 * @return BuildingAddress instance
	 * @throws FlexPayException if building does not have any addresses
	 */
	@Secured (Roles.BUILDING_READ)
	@NotNull
	BuildingAddress findFirstAddress(@NotNull Stub<Building> buildingStub) throws FlexPayException;

	/**
	 * Convert string values to AddressAttribute-instances
	 *
	 * @param number number attribute value
	 * @param bulk bulk attribute value
	 * @return Set of AddressAttributes
	 */
	@NotNull
	Set<AddressAttribute> attributes(@NotNull String number, @Nullable String bulk);

	/**
	 * Find buildings ids for town in range
	 *
	 * @param townStub Town to get buildings for
	 * @param range FetchRange
	 * @return List of buildings in range
	 */
	@Secured (Roles.BUILDING_READ)
	@NotNull
	List<Building> findSimpleByTown(Stub<Town> townStub, FetchRange range);
}
