package org.flexpay.ab.service.imp;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.BuildingsDao;
import org.flexpay.ab.dao.BuildingAttributeTypeDao;
import org.flexpay.ab.dao.BuildingsDaoExt;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class BuildingServiceImpl implements BuildingService {

	private static Logger log = Logger.getLogger(BuildingServiceImpl.class);

	private BuildingsDao buildingsDao;
	private BuildingAttributeTypeDao buildingsTypeDao;
	private BuildingsDaoExt buildingsDaoExt;

	private ParentService<StreetFilter> parentService;

	/**
	 * Setter for property 'buildingsDao'.
	 *
	 * @param buildingsDao Value to set for property 'buildingsDao'.
	 */
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

	public List<Buildings> getBuildings(ArrayStack filters, Page pager) {
		StreetFilter filter = (StreetFilter) filters.peek();
		return buildingsDao.findBuildings(filter.getSelectedId(), pager);
	}
	
	public List<Buildings> getBuildings(Long streetId, Page pager) {
		return buildingsDao.findBuildings(streetId, pager);
	}

	public BuildingsFilter initFilter(BuildingsFilter parentFilter, PrimaryKeyFilter forefatherFilter, Locale locale)
			throws FlexPayException {

		if (parentFilter == null) {
			parentFilter = new BuildingsFilter();
		}

		if (log.isInfoEnabled()) {
			log.info("Getting list buildings, forefather filter: " + forefatherFilter);
		}

		ArrayStack filters = new ArrayStack();
		filters.push(forefatherFilter);
		Page pager = new Page(100000, 1);
		parentFilter.setBuildingses(getBuildings(filters, pager));

		List<Buildings> names = parentFilter.getBuildingses();
		if (names.isEmpty()) {
			throw new FlexPayException("No buildings", "ab.no_buildings");
		}
		if (parentFilter.getSelectedId() == null || !isFilterValid(parentFilter)) {
			Buildings firstObject = names.iterator().next();
			parentFilter.setSelectedId(firstObject.getId());
		}

		return parentFilter;
	}

	private boolean isFilterValid(BuildingsFilter filter) {
		for (Buildings buildings : filter.getBuildingses()) {
			Street street = buildings.getStreet();
			if (street.getId().equals(filter.getSelectedId())) {
				return true;
			}
		}

		return false;
	}

	public ArrayStack initFilters(ArrayStack filters, Locale locale) throws FlexPayException {
		if (filters == null) {
			filters = new ArrayStack();
		}

		BuildingsFilter parentFilter = filters.isEmpty() ? null : (BuildingsFilter) filters.pop();
		filters = parentService.initFilters(filters, locale);
		StreetFilter forefatherFilter = (StreetFilter) filters.peek();

		// init filter
		parentFilter = initFilter(parentFilter, forefatherFilter, locale);
		filters.push(parentFilter);

		return filters;
	}

	private List<BuildingAttributeType> cachedTypes = null;

	/**
	 * Get building attribute type
	 *
	 * @return BuildingAttributeType
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	public BuildingAttributeType getAttributeType(int type) throws FlexPayException {
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
	@Transactional (readOnly = true, rollbackFor = Exception.class)
	public Buildings findBuildings(Street street, District district, String number, String bulk) {
		return buildingsDaoExt.findBuildings(street, district, number, bulk);
	}

	/**
	 * Find building by number
	 *
	 * @param street   Building street
	 * @param number   Building number
	 * @param bulk	 Building bulk number
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	@Transactional (readOnly = true, rollbackFor = Exception.class)
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
	public Buildings getFirstBuildings(Building building) throws FlexPayException {
		List<Buildings> buildingses = buildingsDao.findBuildingBuildings(building.getId(), new Page());
		if (buildingses.isEmpty()) {
			throw new FlexPayException("Building #" + building.getId() + " does not have any buildings");
		}
		return buildingses.get(0);
	}
}
