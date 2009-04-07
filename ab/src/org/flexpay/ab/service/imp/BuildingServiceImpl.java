package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.BuildingDao;
import org.flexpay.ab.dao.BuildingsDao;
import org.flexpay.ab.dao.BuildingsDaoExt;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.ab.persistence.filters.DistrictFilter;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.ModificationListener;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.service.internal.SessionUtils;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Collection;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class BuildingServiceImpl implements BuildingService {

	@NonNls
	private Logger log = LoggerFactory.getLogger(getClass());

	private BuildingDao buildingDao;
	private BuildingsDao buildingsDao;
	private BuildingsDaoExt buildingsDaoExt;

	private ParentService<StreetFilter> parentService;
	private ParentService<DistrictFilter> districtParentService;

	private SessionUtils sessionUtils;
	private ModificationListener<Building> modificationListener;

	public List<BuildingAddress> getBuildings(ArrayStack filters, Page pager) {
		PrimaryKeyFilter streetFilter = (PrimaryKeyFilter) filters.peek();
		if (filters.size() > 1 && filters.peek(1) instanceof DistrictFilter) {
			DistrictFilter districtFilter = (DistrictFilter) filters.peek(1);

			log.debug("Getting district-street buildings");
			return buildingsDao.findStreetDistrictBuildings(streetFilter
					.getSelectedId(), districtFilter.getSelectedId(), pager);
		}

		log.debug("Getting street buildings");
		return buildingsDao.findBuildings(streetFilter.getSelectedId(), pager);
	}

	public List<BuildingAddress> getBuildings(Long streetId, Page pager) {
		return buildingsDao.findBuildings(streetId, pager);
	}

	public List<BuildingAddress> getBuildings(@NotNull Stub<Street> stub) {
		return buildingsDao.findBuildings(stub.getId());
	}

	public BuildingsFilter initFilter(BuildingsFilter parentFilter,
									  PrimaryKeyFilter forefatherFilter, Locale locale)
			throws FlexPayException {

		if (parentFilter == null) {
			parentFilter = new BuildingsFilter();
		}

		log.info("Getting list buildings, forefather filter: {}", forefatherFilter);

		ArrayStack filters = new ArrayStack();
		filters.push(forefatherFilter);
		Page pager = new Page(100000, 1);
		parentFilter.setBuildingses(getBuildings(filters, pager));

		List<BuildingAddress> names = parentFilter.getBuildingses();
		if (names.isEmpty()) {
			throw new FlexPayException("No buildings", "ab.no_buildings");
		}
		if (!isFilterValid(parentFilter)) {
			BuildingAddress firstObject = names.iterator().next();
			parentFilter.setSelectedId(firstObject.getId());
		} else {
			log.debug("Building filter is invalid: {} \n {}", parentFilter, forefatherFilter);
		}

		return parentFilter;
	}

	private boolean isFilterValid(BuildingsFilter filter) {
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

	public ArrayStack initFilters(ArrayStack filters, Locale locale)
			throws FlexPayException {
		if (filters == null) {
			filters = new ArrayStack();
		}

		BuildingsFilter parentFilter = filters.isEmpty() ? null
														 : (BuildingsFilter) filters.pop();

		// check if a districts filter present
		if (filters.size() > 1 && filters.peek(1) instanceof DistrictFilter) {
			PrimaryKeyFilter streetFilter = (PrimaryKeyFilter) filters.pop();

			filters = districtParentService.initFilters(filters, locale);
			DistrictFilter districtFilter = (DistrictFilter) filters.pop();

			filters.push(streetFilter);
			filters = parentService.initFilters(filters, locale);
			streetFilter = (PrimaryKeyFilter) filters.pop();

			filters.push(districtFilter);
			filters.push(streetFilter);

			// init filter
			parentFilter = initFilter(parentFilter, streetFilter,
					districtFilter);
			filters.push(parentFilter);
		} else {
			filters = parentService.initFilters(filters, locale);
			PrimaryKeyFilter forefatherFilter = (PrimaryKeyFilter) filters.peek();

			// init filter
			parentFilter = initFilter(parentFilter, forefatherFilter, locale);
			filters.push(parentFilter);
		}

		return filters;
	}

	private BuildingsFilter initFilter(BuildingsFilter buildingFilter,
									   PrimaryKeyFilter streetFilter, DistrictFilter districtFilter)
			throws FlexPayException {

		if (buildingFilter == null) {
			buildingFilter = new BuildingsFilter();
		}

		log.info("Getting list of buildings, street filter: {}, district filter: {}", streetFilter, districtFilter);

		ArrayStack filters = new ArrayStack();
		filters.push(districtFilter);
		filters.push(streetFilter);
		Page pager = new Page(100000, 1);
		buildingFilter.setBuildingses(getBuildings(filters, pager));

		List<BuildingAddress> names = buildingFilter.getBuildingses();
		if (names.isEmpty()) {
			throw new FlexPayException("No buildings", "ab.no_buildings");
		}
		if (buildingFilter.getSelectedId() == null
			|| !isFilterValid(buildingFilter)) {
			BuildingAddress firstObject = names.iterator().next();
			buildingFilter.setSelectedId(firstObject.getId());
		}

		return buildingFilter;
	}

	/**
	 * Find buildings by attributes
	 *
	 * @param street	 Building street stub
	 * @param district   Building district stub
	 * @param attributes Building attributes
	 * @return Buildings instance, or <code>null</null> if not found
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public BuildingAddress findBuildings(@NotNull Stub<Street> street, @Nullable Stub<District> district,
								   @NotNull Set<AddressAttribute> attributes)
			throws FlexPayException {

		List<BuildingAddress> buildingses =
				district == null ?
				buildingsDaoExt.findBuildings(street.getId(), findNumber(attributes)) :
				buildingsDaoExt.findBuildings(street.getId(), district.getId(), findNumber(attributes));
		buildingses = filter(buildingses, attributes);
		if (buildingses.isEmpty()) {
			return null;
		}
		if (buildingses.size() > 1) {
			throw new FlexPayException("Address duplicates",
					"error.ab.address_duplicates", street.getId(), findNumber(attributes));
		}

		return buildingses.get(0);
	}

	/**
	 * Find all buildings on a specified street
	 *
	 * @param stub Street stub
	 * @return List of buildings on a street
	 */
	@NotNull
	public List<Building> findStreetBuildings(Stub<Street> stub) {
		return buildingDao.findStreetBuildings(stub.getId());
	}

	/**
	 * Find building by number
	 *
	 * @param street   Building street
	 * @param district Building district
	 * @param number   Building number
	 * @param bulk	 Building bulk number
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	@Transactional (readOnly = true, rollbackFor = Exception.class)
	@Nullable
	public BuildingAddress findBuildings(@NotNull Stub<Street> street, @NotNull Stub<District> district, String number, String bulk)
			throws FlexPayException {
		return findBuildings(street, district, attributes(number, bulk));
	}

	/**
	 * Find building by number
	 *
	 * @param streetStub Street stub
	 * @param number	 Building number
	 * @param bulk	   Building bulk number
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	@Transactional (readOnly = true, rollbackFor = Exception.class)
	public BuildingAddress findBuildings(@NotNull Stub<Street> streetStub, @NotNull String number, @Nullable String bulk)
			throws FlexPayException {
		return findBuildings(streetStub, null, attributes(number, bulk));
	}

	/**
	 * Find building by buildings stub
	 *
	 * @param stub Buildings stub
	 * @return Building instance
	 */
	@Nullable
	public Building findBuilding(@NotNull Stub<BuildingAddress> stub) {
		return buildingsDaoExt.findBuilding(stub.getId());
	}

	private Set<AddressAttribute> attributes(@NotNull String number, @Nullable String bulk) {
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

	/**
	 * Find single Building relation for building stub
	 *
	 * @param stub Building stub
	 * @return Buildings instance
	 * @throws FlexPayException if building does not have any buildingses
	 */
	public BuildingAddress getFirstBuildings(Stub<Building> stub) throws FlexPayException {

		List<BuildingAddress> buildingses = buildingsDao.findBuildingBuildings(stub.getId());
		if (buildingses.isEmpty()) {
			throw new FlexPayException("Building #" + stub.getId()
									   + " does not have any buildings");
		}
		return buildingses.get(0);
	}

	/**
	 * Create a new Buildings
	 *
	 * @param street	  Street stub
	 * @param district	District stub
	 * @param numberValue Buildings number
	 * @param bulkValue   Buildings bulk
	 * @return new Buildings object created
	 * @throws FlexPayException if failure occurs
	 */
	@Transactional (readOnly = false, rollbackFor = Exception.class)
	@NotNull
	public BuildingAddress createStreetDistrictBuildings(@NotNull Stub<Street> street, @NotNull Stub<District> district,
												   @NotNull String numberValue, @NotNull String bulkValue)
			throws FlexPayException {
		Building building = new Building();
		building.setDistrict(new District(district.getId()));

		BuildingAddress buildingAddress = new BuildingAddress();
		buildingAddress.setPrimaryStatus(true);
		buildingAddress.setStreet(new Street(street.getId()));
		building.addAddress(buildingAddress);

		buildingAddress.setBuildingAttribute(numberValue, ApplicationConfig.getBuildingAttributeTypeNumber());

		if (StringUtils.isNotBlank(bulkValue)) {
			buildingAddress.setBuildingAttribute(numberValue, ApplicationConfig.getBuildingAttributeTypeNumber());
		}

		buildingDao.create(building);

		return buildingAddress;
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

	@NotNull
	private String findNumber(@NotNull Set<AddressAttribute> attributes) throws FlexPayException {
		for (AddressAttribute attr : attributes) {
			if (attr.getBuildingAttributeType().isBuildingNumber()) {
				return attr.getValue();
			}
		}

		throw new FlexPayException("No number attribute", "error.ab.buildings.no_number");
	}

	private BuildingAddress createBuildings(@Nullable Stub<Building> buildingStub, @Nullable Stub<District> district,
									  @NotNull Stub<Street> street, @NotNull Set<AddressAttribute> attrs)
			throws FlexPayException {

		List<BuildingAddress> buildingses =
				district == null ?
				buildingsDaoExt.findBuildings(street.getId(), findNumber(attrs)) :
				buildingsDaoExt.findBuildings(street.getId(), district.getId(), findNumber(attrs));
		buildingses = filter(buildingses, attrs);
		if (!buildingses.isEmpty()) {
			throw new FlexPayException("Address alredy exists", "error.ab.address_alredy_exist");
		}

		BuildingAddress buildingAddress = new BuildingAddress();
		buildingAddress.setStreet(new Street(street));
		for (AddressAttribute attr : attrs) {
			buildingAddress.setBuildingAttribute(attr.getValue(), attr.getBuildingAttributeType());
		}

		Building building = buildingStub == null ? null : buildingDao.readFull(buildingStub.getId());
		if (building == null) {
			building = new Building();
			building.setDistrict(new District(district));
			buildingAddress.setPrimaryStatus(true);
			building.addAddress(buildingAddress);
			buildingDao.create(building);
		} else {
			building.addAddress(buildingAddress);
			buildingsDao.create(buildingAddress);
		}

		return buildingAddress;
	}

	/**
	 * Create a new Buildings
	 *
	 * @param building Building
	 * @param street   Street
	 * @param attrs	Buildings attributes
	 * @return new Buildings object created
	 */
	@Transactional (readOnly = false)
	@NotNull
	public BuildingAddress createStreetBuildings(@NotNull Stub<Building> building, @NotNull Stub<Street> street,
										   @NotNull Set<AddressAttribute> attrs)
			throws FlexPayException {
		return createBuildings(building, null, street, attrs);
	}

	/**
	 * Create a new Buildings
	 *
	 * @param street   Street
	 * @param district District
	 * @param attrs	Buildings attributes
	 * @return new Buildings object created
	 */
	@Transactional (readOnly = false)
	@NotNull
	public BuildingAddress createStreetDistrictBuildings(@NotNull Stub<Street> street, @NotNull Stub<District> district,
												   @NotNull Set<AddressAttribute> attrs)
			throws FlexPayException {
		return createBuildings(null, district, street, attrs);
	}

	@Nullable
	public BuildingAddress readFull(@NotNull Stub<BuildingAddress> stub) {
		return buildingsDao.readFull(stub.getId());
	}

	/**
	 * Update buildings
	 *
	 * @param buildingAddress Buildings
	 */
	@Transactional (readOnly = false)
	public void update(BuildingAddress buildingAddress) {
		buildingsDao.update(buildingAddress);
	}

	/**
	 * Disable buildings
	 *
	 * @param buildings Buildings to disable
	 */
	@Transactional (readOnly = false)
	public void disable(Collection<Building> buildings) {
		log.info("{} districts to disable", buildings.size());
		for (Building object : buildings) {
			object.disable();
			for (BuildingAddress address : object.getBuildingses()) {
				address.disable();
			}
			buildingDao.update(object);

			modificationListener.onDelete(object);

			log.info("Disabled: {}", object);
		}
	}

	/**
	 * Create building
	 *
	 * @param building Building to create
	 * @return persisted building back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@NotNull
	@Transactional (readOnly = false)
	public Building create(@NotNull Building building) throws FlexPayExceptionContainer {

		validate(building);
		building.setId(null);
		buildingDao.create(building);

		modificationListener.onCreate(building);

		return building;
	}

	/**
	 * Update building
	 *
	 * @param building Building to update
	 * @return updated building back
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation fails
	 */
	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	@NotNull
	@Transactional (readOnly = false)
	public Building update(@NotNull Building building) throws FlexPayExceptionContainer {

		validate(building);

		Building old = read(stub(building));
		if (old == null) {
			throw new FlexPayExceptionContainer(
					new FlexPayException("No object found to update " + building));
		}
		sessionUtils.evict(old);
		modificationListener.onUpdate(old, building);

		buildingDao.update(building);

		return building;
	}

	@SuppressWarnings ({"ThrowableInstanceNeverThrown"})
	private void validate(Building building) throws FlexPayExceptionContainer {

		FlexPayExceptionContainer ex = new FlexPayExceptionContainer();

		if (building.getBuildingses().isEmpty()) {
			ex.addException(new FlexPayException("No address", "error.ab.buildings.no_number"));
		}

		if (building.getDistrict() == null) {
			ex.addException(new FlexPayException("No district", "error.ab.building.no_district"));
		}

		// todo: check that building has only one address on any streets

		if (ex.isNotEmpty()) {
			ex.info(log);
			throw ex;
		}
	}

	/**
	 * Find all Buildings relation for building stub
	 *
	 * @param stub Building stub
	 * @return List of Buildings
	 * @throws FlexPayException if building does not have any buildingses
	 */
	public List<BuildingAddress> getBuildingBuildings(Stub<Building> stub) throws FlexPayException {
		return buildingsDao.findBuildingBuildings(stub.getId());
	}

	public Building read(@NotNull Stub<Building> stub) {
		return buildingDao.readFull(stub.getId());
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
	public void setDistrictParentService(
			ParentService<DistrictFilter> districtParentService) {
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
}
