package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.BuildingAttributeTypeDao;
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
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class BuildingServiceImpl implements BuildingService {

	@NonNls
	private Logger log = Logger.getLogger(getClass());

	private BuildingDao buildingDao;
	private BuildingsDao buildingsDao;
	private BuildingAttributeTypeDao buildingsTypeDao;
	private BuildingsDaoExt buildingsDaoExt;
	private BuildingAttributeTypeDao buildingAttributeTypeDao;

	private ParentService<StreetFilter> parentService;
	private ParentService<DistrictFilter> districtParentService;

	public void setBuildingDao(BuildingDao buildingDao) {
		this.buildingDao = buildingDao;
	}

	public void setBuildingsDao(BuildingsDao buildingsDao) {
		this.buildingsDao = buildingsDao;
	}

	public void setBuildingsTypeDao(BuildingAttributeTypeDao buildingsTypeDao) {
		this.buildingsTypeDao = buildingsTypeDao;
	}

	public void setBuildingsDaoExt(BuildingsDaoExt buildingsDaoExt) {
		this.buildingsDaoExt = buildingsDaoExt;
	}

	/**
	 * Setter for property 'parentService'.
	 *
	 * @param parentService Value to set for property 'parentService'.
	 */
	public void setParentService(ParentService<StreetFilter> parentService) {
		this.parentService = parentService;
	}

	public void setDistrictParentService(
			ParentService<DistrictFilter> districtParentService) {
		this.districtParentService = districtParentService;
	}

	public List<Buildings> getBuildings(ArrayStack filters, Page pager) {
		StreetFilter filter = (StreetFilter) filters.peek();
		if (filters.size() > 1 && filters.peek(1) instanceof DistrictFilter) {
			DistrictFilter districtFilter = (DistrictFilter) filters.peek(1);
			return buildingsDao.findStreetDistrictBuildings(filter
					.getSelectedId(), districtFilter.getSelectedId(), pager);
		}
		return buildingsDao.findBuildings(filter.getSelectedId(), pager);
	}

	public List<Buildings> getBuildings(Long streetId, Page pager) {
		return buildingsDao.findBuildings(streetId, pager);
	}

	public BuildingsFilter initFilter(BuildingsFilter parentFilter,
									  PrimaryKeyFilter forefatherFilter, Locale locale)
			throws FlexPayException {

		if (parentFilter == null) {
			parentFilter = new BuildingsFilter();
		}

		if (log.isInfoEnabled()) {
			log.info("Getting list buildings, forefather filter: "
					 + forefatherFilter);
		}

		ArrayStack filters = new ArrayStack();
		filters.push(forefatherFilter);
		Page pager = new Page(100000, 1);
		parentFilter.setBuildingses(getBuildings(filters, pager));

		List<Buildings> names = parentFilter.getBuildingses();
		if (names.isEmpty()) {
			throw new FlexPayException("No buildings", "ab.no_buildings");
		}
		if (parentFilter.getSelectedId() == null
			|| !isFilterValid(parentFilter)) {
			Buildings firstObject = names.iterator().next();
			parentFilter.setSelectedId(firstObject.getId());
		}

		return parentFilter;
	}

	private boolean isFilterValid(BuildingsFilter filter) {
		if (!filter.needFilter()) {
			return true;
		}
		for (Buildings buildings : filter.getBuildingses()) {
			if (filter.getSelectedStub().getId().equals(buildings.getId())) {
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
			StreetFilter streetFilter = (StreetFilter) filters.pop();

			filters = districtParentService.initFilters(filters, locale);
			DistrictFilter districtFilter = (DistrictFilter) filters.pop();

			filters.push(streetFilter);
			filters = parentService.initFilters(filters, locale);
			streetFilter = (StreetFilter) filters.pop();

			filters.push(districtFilter);
			filters.push(streetFilter);

			// init filter
			parentFilter = initFilter(parentFilter, streetFilter,
					districtFilter);
			filters.push(parentFilter);
		} else {
			filters = parentService.initFilters(filters, locale);
			StreetFilter forefatherFilter = (StreetFilter) filters.peek();

			// init filter
			parentFilter = initFilter(parentFilter, forefatherFilter, locale);
			filters.push(parentFilter);
		}

		return filters;
	}

	private BuildingsFilter initFilter(BuildingsFilter buildingFilter,
									   StreetFilter streetFilter, DistrictFilter districtFilter)
			throws FlexPayException {

		if (buildingFilter == null) {
			buildingFilter = new BuildingsFilter();
		}

		if (log.isInfoEnabled()) {
			log.info("Getting list of buildings, street filter: "
					 + streetFilter + ", district filter: " + districtFilter);
		}

		ArrayStack filters = new ArrayStack();
		filters.push(districtFilter);
		filters.push(streetFilter);
		Page pager = new Page(100000, 1);
		buildingFilter.setBuildingses(getBuildings(filters, pager));

		List<Buildings> names = buildingFilter.getBuildingses();
		if (names.isEmpty()) {
			throw new FlexPayException("No buildings", "ab.no_buildings");
		}
		if (buildingFilter.getSelectedId() == null
			|| !isFilterValid(buildingFilter)) {
			Buildings firstObject = names.iterator().next();
			buildingFilter.setSelectedId(firstObject.getId());
		}

		return buildingFilter;
	}

	/**
	 * Get building attribute types
	 *
	 * @return BuildingAttributeType list
	 */
	public List<BuildingAttributeType> getAttributeTypes() {

		return buildingsTypeDao.findAttributeTypes();
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
	public Buildings findBuildings(@NotNull Stub<Street> street, @Nullable Stub<District> district,
								   @NotNull Set<BuildingAttribute> attributes)
			throws FlexPayException {

		List<Buildings> buildingses =
				district == null ?
				buildingsDaoExt.findBuildings(street.getId(), findNumber(attributes)) :
				buildingsDaoExt.findBuildings(street.getId(), district.getId(), findNumber(attributes));
		buildingses = filter(buildingses, attributes);
		if (buildingses.isEmpty()) {
			return null;
		}
		if (buildingses.size() > 1) {
			System.out.println(String.format("Duplicates %s", buildingses));
			throw new FlexPayException("Adress duplicates",
					"error.ab.address_duplicates", street.getId(), findNumber(attributes));
		}

		return buildingses.get(0);
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
	public Buildings findBuildings(@NotNull Stub<Street> street, @NotNull Stub<District> district, String number, String bulk)
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
	public Buildings findBuildings(@NotNull Stub<Street> streetStub, @NotNull String number, @Nullable String bulk)
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
	public Building findBuilding(@NotNull Stub<Buildings> stub) {
		return buildingsDaoExt.findBuilding(stub.getId());
	}

	private Set<BuildingAttribute> attributes(@NotNull String number, @Nullable String bulk) {
		Set<BuildingAttribute> result = set();

		BuildingAttribute numberAttr = new BuildingAttribute();
		numberAttr.setValue(number);
		numberAttr.setBuildingAttributeType(ApplicationConfig.getBuildingAttributeTypeNumber());
		result.add(numberAttr);

		if (StringUtils.isNotEmpty(bulk)) {
			BuildingAttribute bulkAttr = new BuildingAttribute();
			bulkAttr.setValue(number);
			bulkAttr.setBuildingAttributeType(ApplicationConfig.getBuildingAttributeTypeBulk());
			result.add(bulkAttr);
		}

		return result;
	}

	/**
	 * Find single Building relation for building stub
	 *
	 * @param building Building stub
	 * @return Buildings instance
	 * @throws FlexPayException if building does not have any buildingses
	 */
	public Buildings getFirstBuildings(Building building)
			throws FlexPayException {
		List<Buildings> buildingses = buildingsDao.findBuildingBuildings(
				building.getId(), new Page());
		if (buildingses.isEmpty()) {
			throw new FlexPayException("Building #" + building.getId()
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
	public Buildings createStreetDistrictBuildings(@NotNull Stub<Street> street, @NotNull Stub<District> district,
												   @NotNull String numberValue, @NotNull String bulkValue)
			throws FlexPayException {
		Building building = new Building();
		building.setDistrict(new District(district.getId()));

		Buildings buildings = new Buildings();
		buildings.setPrimaryStatus(true);
		buildings.setStreet(new Street(street.getId()));
		building.addBuildings(buildings);

		buildings.setBuildingAttribute(numberValue, ApplicationConfig.getBuildingAttributeTypeNumber());

		if (StringUtils.isNotBlank(bulkValue)) {
			buildings.setBuildingAttribute(numberValue, ApplicationConfig.getBuildingAttributeTypeNumber());
		}

		buildingDao.create(building);

		return buildings;
	}

	@NotNull
	private List<Buildings> filter(@NotNull List<Buildings> buildingses, @NotNull Set<BuildingAttribute> attrs) {
		List<Buildings> result = list();
		for (Buildings buildings : buildingses) {
			if (buildings.hasSameAttributes(attrs)) {
				result.add(buildings);
			}
		}

		return result;
	}

	@NotNull
	private String findNumber(@NotNull Set<BuildingAttribute> attributes) throws FlexPayException {
		for (BuildingAttribute attr : attributes) {
			if (attr.getBuildingAttributeType().isBuildingNumber()) {
				return attr.getValue();
			}
		}

		throw new FlexPayException("No number attribute", "error.ab.buildings.no_number");
	}

	private Buildings createBuildings(@Nullable Stub<Building> buildingStub, @Nullable Stub<District> district,
									  @NotNull Stub<Street> street, @NotNull Set<BuildingAttribute> attrs)
			throws FlexPayException {

		List<Buildings> buildingses =
				district == null ?
				buildingsDaoExt.findBuildings(street.getId(), findNumber(attrs)) :
				buildingsDaoExt.findBuildings(street.getId(), district.getId(), findNumber(attrs));
		buildingses = filter(buildingses, attrs);
		if (!buildingses.isEmpty()) {
			throw new FlexPayException("Address alredy exists", "error.ab.adress_alredy_exist");
		}

		Buildings buildings = new Buildings();
		buildings.setStreet(new Street(street));
		for (BuildingAttribute attr : attrs) {
			buildings.setBuildingAttribute(attr.getValue(), attr.getBuildingAttributeType());
		}

		Building building = buildingStub == null ? null : buildingDao.readFull(buildingStub.getId());
		if (building == null) {
			building = new Building();
			building.setDistrict(new District(district));
			buildings.setPrimaryStatus(true);
			building.addBuildings(buildings);
			buildingDao.create(building);
		} else {
			building.addBuildings(buildings);
			buildingsDao.create(buildings);
		}

		return buildings;
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
	public Buildings createStreetBuildings(@NotNull Stub<Building> building, @NotNull Stub<Street> street,
										   @NotNull Set<BuildingAttribute> attrs)
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
	public Buildings createStreetDistrictBuildings(@NotNull Stub<Street> street, @NotNull Stub<District> district,
												   @NotNull Set<BuildingAttribute> attrs)
			throws FlexPayException {
		return createBuildings(null, district, street, attrs);
	}

	@Nullable
	public Buildings readFull(@NotNull Stub<Buildings> stub) {
		return buildingsDao.readFull(stub.getId());
	}

	/**
	 * Get building attribute type
	 *
	 * @param stub BuildingAttributeType stub
	 * @return Attribute type if found, or <code>null</code> otherwise
	 */
	@Nullable
	public BuildingAttributeType getAttributeType(@NotNull Stub<BuildingAttributeType> stub) {
		return buildingAttributeTypeDao.readFull(stub.getId());
	}

	/**
	 * Update buildings
	 *
	 * @param buildings Buildings
	 */
	@Transactional (readOnly = false)
	public void update(Buildings buildings) {
		buildingsDao.update(buildings);
	}

	/**
	 * Find all Buildings relation for building stub
	 *
	 * @param building Building stub
	 * @return List of Buildings
	 * @throws FlexPayException if building does not have any buildingses
	 */
	public List<Buildings> getBuildingBuildings(Building building)
			throws FlexPayException {
		return buildingsDao.findBuildingBuildings(building.getId(), new Page());
	}

	public Building readBuilding(Long id) {
		return buildingDao.read(id);
	}

	@Transactional (readOnly = false)
	public BuildingAttributeType createBuildingAttributeType(BuildingAttributeType type) {
		Long id = buildingAttributeTypeDao.create(type);
		type.setId(id);

		return type;
	}

	@Transactional (readOnly = false)
	public void updateBuildingAttributeType(BuildingAttributeType type) {
		buildingAttributeTypeDao.update(type);
	}

	/**
	 * @param buildingAttributeTypeDao the buildingAttributeTypeDao to set
	 */
	public void setBuildingAttributeTypeDao(
			BuildingAttributeTypeDao buildingAttributeTypeDao) {
		this.buildingAttributeTypeDao = buildingAttributeTypeDao;
	}
}
