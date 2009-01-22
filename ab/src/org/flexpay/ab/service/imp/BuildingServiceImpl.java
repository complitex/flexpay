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
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class BuildingServiceImpl implements BuildingService {

	@NonNls
	private Logger log = LoggerFactory.getLogger(getClass());

	private BuildingDao buildingDao;
	private BuildingsDao buildingsDao;
	private BuildingsDaoExt buildingsDaoExt;

	private ParentService<StreetFilter> parentService;
	private ParentService<DistrictFilter> districtParentService;

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
		building.addBuildings(buildingAddress);

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
			throw new FlexPayException("Address alredy exists", "error.ab.adress_alredy_exist");
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
			building.addBuildings(buildingAddress);
			buildingDao.create(building);
		} else {
			building.addBuildings(buildingAddress);
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
		return buildingDao.read(stub.getId());
	}

	public void setBuildingDao(BuildingDao buildingDao) {
		this.buildingDao = buildingDao;
	}

	public void setBuildingsDao(BuildingsDao buildingsDao) {
		this.buildingsDao = buildingsDao;
	}

	public void setBuildingsDaoExt(BuildingsDaoExt buildingsDaoExt) {
		this.buildingsDaoExt = buildingsDaoExt;
	}

	public void setParentService(ParentService<StreetFilter> parentService) {
		this.parentService = parentService;
	}

	public void setDistrictParentService(
			ParentService<DistrictFilter> districtParentService) {
		this.districtParentService = districtParentService;
	}
}
