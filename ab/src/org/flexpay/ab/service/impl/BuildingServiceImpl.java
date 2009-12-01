package org.flexpay.ab.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.dao.BuildingsDao;
import org.flexpay.ab.dao.BuildingsDaoExt;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.ab.persistence.filters.DistrictFilter;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.PropertiesInitializer;
import org.flexpay.common.service.PropertiesInitializerHolder;
import org.flexpay.common.service.internal.SessionUtils;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Transactional (readOnly = true)
public class BuildingServiceImpl implements BuildingService, ParentService<BuildingsFilter> {

	private Logger log = LoggerFactory.getLogger(getClass());

	private BuildingDao buildingDao;
	private BuildingsDao buildingsDao;
	private BuildingsDaoExt buildingsDaoExt;

	private PropertiesInitializerHolder<Building> propertiesInitializerHolder;

	private AddressService addressService;
	private ParentService<StreetFilter> parentService;
	private ParentService<DistrictFilter> districtParentService;

	private SessionUtils sessionUtils;
	private ModificationListener<Building> modificationListener;

	/**
	 * Read building
	 *
	 * @param buildingStub Building stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	public Building readFull(@NotNull Stub<Building> buildingStub) {
		Building building = buildingDao.readFull(buildingStub.getId());
		if (building == null) {
			return null;
		}

		for (PropertiesInitializer<Building> initializer : propertiesInitializerHolder.getInitializers()) {
			initializer.init(building);
		}
		return building;
	}

	/**
	 * Read buildings collection by theirs ids
	 *
 	 * @param buildingIds Building ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found buildings
	 */
	@NotNull
	@Override
	public List<Building> readFull(@NotNull Collection<Long> buildingIds, boolean preserveOrder) {

		List<Building> buildings = buildingDao.readFullCollection(buildingIds, preserveOrder);

		for (PropertiesInitializer<Building> initializer : propertiesInitializerHolder.getInitializers()) {
			initializer.init(buildings);
		}

		return buildings;
	}

	/**
	 * Disable buildings
	 *
	 * @param buildingIds IDs of buildings to disable
	 */
	@Transactional (readOnly = false)
	@Override
	public void disable(@NotNull Collection<Long> buildingIds) {
		for (Long id : buildingIds) {
			Building building = buildingDao.read(id);
			if (building == null) {
				log.warn("Can't get building with id {} from DB", id);
				continue;
			}
			building.disable();
			for (BuildingAddress address : building.getBuildingses()) {
				address.disable();
			}
			buildingDao.update(building);

			modificationListener.onDelete(building);

			log.debug("Building disabled: {}", building);
		}
	}

	/**
	 * Create building
	 *
	 * @param building Building to save
	 * @return Saved instance of building
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	@Transactional (readOnly = false)
	@Override
	public Building create(@NotNull Building building) throws FlexPayExceptionContainer {

		validate(building);
		building.setId(null);
		buildingDao.create(building);

		modificationListener.onCreate(building);

		return building;
	}

	/**
	 * Update or create building
	 *
	 * @param building Building to save
	 * @return Saved instance of building
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@Transactional (readOnly = false)
	@NotNull
	@Override
	public Building update(@NotNull Building building) throws FlexPayExceptionContainer {

		validate(building);

		Building old = readFull(stub(building));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No building found to update " + building));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, building);

		building = buildingDao.merge(building);

		return building;
	}

	/**
	 * Validate building before save
	 *
	 * @param building Building object to validate
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(Building building) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer container = new FlexPayExceptionContainer();

		if (building.getBuildingses().isEmpty()) {
			container.addException(new FlexPayException("No address", "ab.error.building.no_number"));
		}

		if (building.getDistrict() == null) {
			container.addException(new FlexPayException("No district", "ab.error.building.no_district"));
		}

		if (building.isNotNew()) {
			Building old = readFull(stub(building));
			sessionUtils.evict(old);
			if (!old.getDistrictStub().equals(building.getDistrictStub())) {
				container.addException(new FlexPayException("District changed", "error.ab.building.district_changed"));
			}
		}

		// check that building has only one address on any streets
		do {
			Set<Stub<Street>> streetStubs = set();
			for (BuildingAddress address : building.getBuildingses()) {
				if (streetStubs.contains(address.getStreetStub())) {
					container.addException(new FlexPayException("Two street address", "ab.error.building.street_address_duplicate"));
					break;
				}
				streetStubs.add(address.getStreetStub());
			}
		} while (false);

		// check no other buildings has the same address
		for (BuildingAddress address : building.getBuildingses()) {
			String number = address.getNumber();
			if (number == null) {
				log.warn("Incorrect building address. Address number is null. Address: {}", address);
				continue;
			}
			List<BuildingAddress> candidates = buildingsDaoExt.findBuildings(address.getStreetStub().getId(), number);
			candidates = filter(candidates, address.getBuildingAttributes());
			if (!candidates.isEmpty() && (candidates.size() > 1 || !candidates.get(0).equals(address))) {
				String addressStr = "";
				try {
					addressStr = addressService.getBuildingsAddress(stub(candidates.get(0)), null);
				} catch (Exception e) {
					// do nothing
				}

				container.addException(new FlexPayException("Address already exists", "ab.error.building.address_already_exists", addressStr));
			}
		}

		if (container.isNotEmpty()) {
			throw container;
		}
	}

	/**
	 * Find building by building address stub
	 *
	 * @param addressStub BuildingAddress stub
	 * @return Building instance if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	public Building findBuilding(@NotNull Stub<BuildingAddress> addressStub) {
		Building building = buildingsDaoExt.findBuilding(addressStub.getId());
		sessionUtils.evict(building);
		return building != null ? readFull(stub(building)) : null;
	}

	/**
	 * Read building address
	 *
	 * @param addressStub BuildingAddress stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	public BuildingAddress readFullAddress(@NotNull Stub<BuildingAddress> addressStub) {
		return buildingsDao.readFull(addressStub.getId());
	}

	/**
	 * Read building address with its full hierarchical structure:
	 * country-region-town-street
	 *
	 * @param addressStub Building address stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	public BuildingAddress readWithHierarchy(@NotNull Stub<BuildingAddress> addressStub) {
		List<BuildingAddress> addresses = buildingsDao.findWithFullHierarchy(addressStub.getId());
		return addresses.isEmpty() ? null : addresses.get(0);
	}

	/**
	 * Read building addresses collection by theirs ids
	 *
 	 * @param addressIds BuildingAddress ids
	 * @param preserveOrder Whether to preserve order of objects
	 * @return Found building addresses
	 */
	@NotNull
	@Override
	public List<BuildingAddress> readFullAddresses(@NotNull Collection<Long> addressIds, boolean preserveOrder) {
		return buildingsDao.readFullCollection(addressIds, preserveOrder);
	}

	/**
	 * Disable building addresses
	 *
	 * @param addressIds IDs of building addresses to disable
	 * @param buildingStub Addresses building stub
	 */
	@Transactional (readOnly = false)
	@Override
	public void disableAddresses(@NotNull Collection<Long> addressIds, @NotNull Stub<Building> buildingStub) {

		Building building = buildingDao.readFull(buildingStub.getId());
		if (building == null) {
			log.warn("Can't get building with id {} from DB", buildingStub.getId());
			return;
		}
		sessionUtils.evict(building);

		for (Long id : addressIds) {
			BuildingAddress address = building.getAddress(new Stub<BuildingAddress>(id));
			if (address == null) {
				log.warn("Can't get building address with id {}", id);
				continue;
			}
			if (address.getPrimaryStatus()) {
				log.warn("Can't disable building address with id {}, because its primary address for building with id {}", id, buildingStub.getId());
				continue;
			}
			address.disable();

			log.debug("Building address disabled: {}", address);
		}

		try {
			update(building);
		} catch (FlexPayExceptionContainer flexPayExceptionContainer) {
			// do nothing
		}

	}

	/**
	 * Get a list of available building addresses
	 *
	 * @param filters Parent filters
	 * @param sorters Stack of sorters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@NotNull
	@Override
	public List<BuildingAddress> findAddresses(@NotNull ArrayStack filters, @NotNull List<? extends ObjectSorter> sorters, @NotNull Page<BuildingAddress> pager) {
		return buildingsDaoExt.findBuildingAddresses(filters, sorters, pager);
	}

	/**
	 * Get a list of available building addresses
	 *
	 * @param filters Parent filters
	 * @param pager   Page
	 * @return List of Objects
	 */
	@NotNull
	@Override
	public List<BuildingAddress> findAddresses(@NotNull ArrayStack filters, Page<BuildingAddress> pager) {
		PrimaryKeyFilter<?> streetFilter = (PrimaryKeyFilter<?>) filters.peek();
		if (filters.size() > 1 && filters.peek(1) instanceof DistrictFilter) {
			DistrictFilter districtFilter = (DistrictFilter) filters.peek(1);

			log.debug("Getting district-street buildings");
			return buildingsDao.findStreetDistrictBuildings(streetFilter
					.getSelectedId(), districtFilter.getSelectedId(), pager);
		}

		log.debug("Getting street buildings");
		return buildingsDao.findBuildings(streetFilter.getSelectedId(), pager);
	}

	/**
	 * Lookup building address by street id.
	 *
	 * @param streetStub  Street stub
	 * @return List of found building addresses
	 */
	@NotNull
	@Override
	public List<BuildingAddress> findAddressesByParent(@NotNull Stub<Street> streetStub) {
		return buildingsDao.findBuildings(streetStub.getId());
	}

	/**
	 * Find building addresses for building
	 *
	 * @param buildingStub Building stub
	 * @return List of building addresses
	 * @throws FlexPayException if building does not have any buildingses
	 */
	@NotNull
	@Override
	public List<BuildingAddress> findAddresesByBuilding(@NotNull Stub<Building> buildingStub) throws FlexPayException {
		return buildingsDao.findBuildingBuildings(buildingStub.getId());
	}

	/**
	 * Find building addresses by street and attributes
	 *
	 * @param streetStub Building street stub
	 * @param attributes Building attributes
	 * @return BuildingAddress instance, or <code>null</null> if not found
	 * @throws FlexPayException if failure occurs
	 */
	@Nullable
	@Override
	public BuildingAddress findAddresses(@NotNull Stub<Street> streetStub, @NotNull Set<AddressAttribute> attributes) throws FlexPayException {
		return findAddresses(streetStub, null, attributes);
	}

	/**
	 * Find building addresses by street, district and attributes
	 *
	 * @param streetStub Building street stub
	 * @param districtStub Building district stub
	 * @param attributes Building attributes
	 * @return BuildingAddress instance, or <code>null</null> if not found
	 * @throws FlexPayException if failure occurs
	 */
	@Nullable
	@Override
	public BuildingAddress findAddresses(@NotNull Stub<Street> streetStub, @Nullable Stub<District> districtStub,
										 @NotNull Set<AddressAttribute> attributes) throws FlexPayException {

		List<BuildingAddress> buildingses =
				districtStub == null ?
				buildingsDaoExt.findBuildings(streetStub.getId(), findNumber(attributes)) :
				buildingsDaoExt.findBuildings(streetStub.getId(), districtStub.getId(), findNumber(attributes));
		buildingses = filter(buildingses, attributes);
		if (buildingses.isEmpty()) {
			return null;
		}
		if (buildingses.size() > 1) {
			throw new FlexPayException("Address duplicates",
					"error.ab.address_duplicates", streetStub.getId(), findNumber(attributes));
		}

		return buildingses.get(0);
	}

	@NotNull
	private String findNumber(@NotNull Set<AddressAttribute> attributes) throws FlexPayException {
		for (AddressAttribute attr : attributes) {
			if (attr.getBuildingAttributeType().isBuildingNumber()) {
				return attr.getValue();
			}
		}

		throw new FlexPayException("No number attribute", "ab.error.building.no_number");
	}

	@NotNull
	private List<BuildingAddress> filter(@NotNull List<BuildingAddress> buildingses, @NotNull Set<AddressAttribute> attrs) {
		List<BuildingAddress> result = list();
		for (BuildingAddress buildingAddress : buildingses) {
			if (buildingAddress.hasSameAttributes(attrs)) {
				result.add(buildingAddress);
			}
		}

		return result;
	}

	/**
	 * Find single Building relation for building stub
	 *
	 * @param buildingStub Building stub
	 * @return BuildingAddress instance
	 * @throws FlexPayException if building does not have any addresses
	 */
	@NotNull
	@Override
	public BuildingAddress findFirstAddress(@NotNull Stub<Building> buildingStub) throws FlexPayException {

		List<BuildingAddress> addresses = buildingsDao.findBuildingBuildings(buildingStub.getId());
		if (addresses.isEmpty()) {
			throw new FlexPayException("Building #" + buildingStub.getId() + " doesn't have any addresses");
		}
		return addresses.get(0);
	}

	/**
	 * Convert string values to AddressAttribute-instances
	 *
	 * @param number number attribute value
	 * @param bulk bulk attribute value
	 * @return Set of AddressAttributes
	 */
	@NotNull
	@Override
	public Set<AddressAttribute> attributes(@NotNull String number, @Nullable String bulk) {
		Set<AddressAttribute> result = set();

		AddressAttribute numberAttr = new AddressAttribute();
		numberAttr.setValue(number);
		numberAttr.setBuildingAttributeType(ApplicationConfig.getBuildingAttributeTypeNumber());
		result.add(numberAttr);

		if (StringUtils.isNotEmpty(bulk)) {
			AddressAttribute bulkAttr = new AddressAttribute();
			bulkAttr.setValue(bulk);
			bulkAttr.setBuildingAttributeType(ApplicationConfig.getBuildingAttributeTypeBulk());
			result.add(bulkAttr);
		}

		return result;
	}

	@NotNull
	@Override
	public List<Building> findSimpleByTown(Stub<Town> townStub, FetchRange range) {
		return buildingDao.findSimpleByTown(townStub.getId(), range);
	}

	/**
	 * Initialize parent filter. Possibly taking in account upper level forefather filter
	 *
	 * @param parentFilter	 Filter to init
	 * @param forefatherFilter Upper level filter
	 * @param locale		   Locale to get parent names in
	 * @return Initialised filter
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	@Override
	public BuildingsFilter initFilter(@Nullable BuildingsFilter parentFilter, @NotNull PrimaryKeyFilter<?> forefatherFilter, @NotNull Locale locale)
			throws FlexPayException {

		if (parentFilter == null) {
			parentFilter = new BuildingsFilter();
		}

		log.info("Getting list buildings, forefather filter: {}", forefatherFilter);

		ArrayStack filters = new ArrayStack();
		filters.push(forefatherFilter);
		parentFilter.setBuildingses(findAddresses(filters, new Page<BuildingAddress>(100000, 1)));

		List<BuildingAddress> names = parentFilter.getBuildingses();
		if (names.isEmpty()) {
			throw new FlexPayException("No buildings", "ab.error.building.no_buildings");
		}
		if (!isFilterValid(parentFilter)) {
			BuildingAddress firstObject = names.iterator().next();
			parentFilter.setSelectedId(firstObject.getId());
		} else {
			log.debug("Building filter is invalid: {} \n {}", parentFilter, forefatherFilter);
		}

		return parentFilter;
	}

	private boolean isFilterValid(@NotNull BuildingsFilter filter) {
		if (!filter.needFilter()) {
			return true;
		}
		for (BuildingAddress buildingAddress : filter.getBuildingses()) {
			if (filter.getSelectedStub().getId().equals(buildingAddress.getId())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Initialize filters. <p>Filters are coming from the most significant to less significant ones order, like
	 * CountryFilter, RegionFilter, TownFilter for example</p>
	 *
	 * @param filters Filters to init
	 * @param locale  Locale to get parent names in
	 * @return Initialised filters stack
	 * @throws FlexPayException if failure occurs
	 */
	@NotNull
	@Override
	public ArrayStack initFilters(@Nullable ArrayStack filters, @NotNull Locale locale) throws FlexPayException {

		if (filters == null) {
			filters = new ArrayStack();
		}

		BuildingsFilter parentFilter = filters.isEmpty() ? null : (BuildingsFilter) filters.pop();

		// check if a districts filter present
		if (filters.size() > 1 && filters.peek(1) instanceof DistrictFilter) {
			PrimaryKeyFilter<?> streetFilter = (PrimaryKeyFilter<?>) filters.pop();

			filters = districtParentService.initFilters(filters, locale);
			DistrictFilter districtFilter = (DistrictFilter) filters.pop();

			filters.push(streetFilter);
			filters = parentService.initFilters(filters, locale);
			streetFilter = (PrimaryKeyFilter<?>) filters.pop();

			filters.push(districtFilter);
			filters.push(streetFilter);

			// init filter
			parentFilter = initFilter(parentFilter, streetFilter,
					districtFilter);
			filters.push(parentFilter);
		} else {
			filters = parentService.initFilters(filters, locale);
			PrimaryKeyFilter<?> forefatherFilter = (PrimaryKeyFilter<?>) filters.peek();

			// init filter
			parentFilter = initFilter(parentFilter, forefatherFilter, locale);
			filters.push(parentFilter);
		}

		return filters;
	}

	@NotNull
	private BuildingsFilter initFilter(@Nullable BuildingsFilter buildingFilter,
									   @NotNull PrimaryKeyFilter<?> streetFilter, @NotNull DistrictFilter districtFilter) throws FlexPayException {

		if (buildingFilter == null) {
			buildingFilter = new BuildingsFilter();
		}

		log.info("Getting list of buildings, street filter: {}, district filter: {}", streetFilter, districtFilter);

		ArrayStack filters = new ArrayStack();
		filters.push(districtFilter);
		filters.push(streetFilter);
		buildingFilter.setBuildingses(findAddresses(filters, new Page<BuildingAddress>(100000, 1)));

		List<BuildingAddress> names = buildingFilter.getBuildingses();
		if (names.isEmpty()) {
			throw new FlexPayException("No buildings", "ab.error.building.no_buildings");
		}
		if (buildingFilter.getSelectedId() == null || !isFilterValid(buildingFilter)) {
			BuildingAddress firstObject = names.iterator().next();
			buildingFilter.setSelectedId(firstObject.getId());
		}

		return buildingFilter;
	}

	@Required
	public void setBuildingDao(BuildingDao buildingDao) {
		this.buildingDao = buildingDao;
	}

	@Required
	public void setBuildingsDao(BuildingsDao buildingsDao) {
		this.buildingsDao = buildingsDao;
	}

	@Required
	public void setBuildingsDaoExt(BuildingsDaoExt buildingsDaoExt) {
		this.buildingsDaoExt = buildingsDaoExt;
	}

	@Required
	public void setParentService(ParentService<StreetFilter> parentService) {
		this.parentService = parentService;
	}

	@Required
	public void setDistrictParentService(ParentService<DistrictFilter> districtParentService) {
		this.districtParentService = districtParentService;
	}

	@Required
	public void setSessionUtils(SessionUtils sessionUtils) {
		this.sessionUtils = sessionUtils;
	}

	@Required
	public void setModificationListener(ModificationListener<Building> modificationListener) {
		this.modificationListener = modificationListener;
	}

	@Required
	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

	@Required
	public void setPropertiesInitializerHolder(PropertiesInitializerHolder<Building> propertiesInitializerHolder) {
		this.propertiesInitializerHolder = propertiesInitializerHolder;
	}

}
