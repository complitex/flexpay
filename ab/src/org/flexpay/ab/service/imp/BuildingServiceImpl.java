package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.*;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.ab.persistence.filters.DistrictFilter;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class BuildingServiceImpl implements BuildingService {

	private static Logger log = Logger.getLogger(BuildingServiceImpl.class);

	private BuildingDao buildingDao;
	private BuildingsDao buildingsDao;
	private BuildingAttributeTypeDao buildingsTypeDao;
	private BuildingsDaoExt buildingsDaoExt;

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
		for (Buildings buildings : filter.getBuildingses()) {
			if (buildings.getId().equals(filter.getSelectedId())) {
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

	private List<BuildingAttributeType> cachedTypes = null;

	/**
	 * Get building attribute type
	 *
	 * @return BuildingAttributeType
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public BuildingAttributeType getAttributeType(int type)
			throws FlexPayException {
		if (cachedTypes == null) {
			cachedTypes = buildingsTypeDao.findAttributeTypes();
		}
		for (BuildingAttributeType attributeType : cachedTypes) {
			if (attributeType.getType() == type) {
				return attributeType;
			}
		}

		throw new FlexPayException("Unknown building attribute type: " + type);
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
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public Buildings findBuildings(Street street, District district,
								   String number, String bulk) {
		return buildingsDaoExt.findBuildings(street, district, number, bulk);
	}

	/**
	 * Find building by number
	 *
	 * @param street Building street
	 * @param number Building number
	 * @param bulk   Building bulk number
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public Buildings findBuildings(Street street, String number, String bulk) {
		return buildingsDaoExt.findBuildings(street, number, bulk);
	}

	/**
	 * Find building by buildings stub
	 *
	 * @param buildingsStub object with id only
	 * @return Building instance
	 */
	public Building findBuilding(Buildings buildingsStub) {
		return buildingsDaoExt.findBuilding(buildingsStub);
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
	 * @param street	  Street
	 * @param district	District
	 * @param numberValue Buildings number
	 * @param bulkValue   Buildings bulk
	 * @return new Buildings object created
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Buildings createBuildings(Street street, District district,
									 String numberValue, String bulkValue) throws FlexPayException {
		Building building = new Building();
		building.setDistrict(district);

		Buildings buildings = new Buildings();
		buildings.setPrimaryStatus(true);
		buildings.setStreet(street);
		building.addBuildings(buildings);

		buildings.setBuildingAttribute(numberValue, getAttributeType(BuildingAttributeType.TYPE_NUMBER));

		if (StringUtils.isNotBlank(bulkValue)) {
			buildings.setBuildingAttribute(numberValue, getAttributeType(BuildingAttributeType.TYPE_BULK));
		}

		buildingDao.create(building);

		return buildings;
	}

	/**
	 * Create a new Buildings
	 *
	 * @param building	Building
	 * @param street	  Street
	 * @param numberValue Buildings number
	 * @param bulkValue   Buildings bulk
	 * @return new Buildings object created
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Buildings createBuildings(Building building, Street street,
									 String numberValue, String bulkValue) throws FlexPayException {

		Buildings buildings = new Buildings();
		buildings.setStreet(street);
		building.addBuildings(buildings);

		buildings.setBuildingAttribute(numberValue, getAttributeType(BuildingAttributeType.TYPE_NUMBER));

		if (StringUtils.isNotBlank(bulkValue)) {
			buildings.setBuildingAttribute(numberValue, getAttributeType(BuildingAttributeType.TYPE_BULK));
		}

		buildingsDao.create(buildings);

		return buildings;
	}

	public Buildings readFull(Long buildingsId) {
		return buildingsDao.readFull(buildingsId);
	}

	/**
	 * Update buildings
	 *
	 * @param buildings Buildings
	 */
	@Transactional(readOnly = false)
	public void update(Buildings buildings) {
		/*
		 * Buildings persistence = buildingsDao.read(buildings.getId()); if
		 * (buildings.getNumberAttribute() == null) {
		 * buildingAttributeDao.delete(persistence.getNumberAttribute()); } if
		 * (buildings.getBulkAttribute() == null) {
		 * buildingAttributeDao.delete(persistence.getBulkAttribute()); }
		 */
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
}
