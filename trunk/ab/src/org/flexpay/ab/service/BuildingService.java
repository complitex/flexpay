package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.exception.FlexPayException;

import java.util.List;
import java.util.Locale;

public interface BuildingService extends ParentService<BuildingsFilter> {

	public List<Buildings> getBuildings(ArrayStack filters, Page pager);

	public List<Buildings> getBuildings(Long streetId, Page pager);

	/**
	 * Get building attribute type
	 * 
	 * @param type
	 *            Attribute type
	 * @return BuildingAttributeType
	 * @throws FlexPayException
	 *             if failure occurs
	 */
	public BuildingAttributeType getAttributeType(int type)
			throws FlexPayException;

	/**
	 * Find building by number
	 * 
	 * @param street
	 *            Building street
	 * @param district
	 *            Building district
	 * @param number
	 *            Building number
	 * @param bulk
	 *            Building bulk number
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	Buildings findBuildings(Street street, District district, String number,
			String bulk);

	/**
	 * Find building by number
	 * 
	 * @param street
	 *            Building street
	 * @param number
	 *            Building number
	 * @param bulk
	 *            Building bulk number
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	Buildings findBuildings(Street street, String number, String bulk);

	/**
	 * Find building by buildings stub
	 * 
	 * @param buildingsStub
	 *            object with id only
	 * @return Building instance
	 */
	Building findBuilding(Buildings buildingsStub);

	/**
	 * Find single Building relation for building stub
	 * 
	 * @param building
	 *            Building stub
	 * @return Buildings instance
	 * @throws FlexPayException
	 *             if building does not have any buildingses
	 */
	Buildings getFirstBuildings(Building building) throws FlexPayException;
	
	Buildings readFull(Long buildingsId);

	/**
	 * Update buildings
	 * @param buildings Buildings
	 * @param toDelete List of BuildingAttribute to delete or <code>null</null> if nothing to delete
	 */
	void update(Buildings buildings, List<BuildingAttribute> toDelete);

	/**
	 * Create a new Buildings
	 *
	 * @param street Street
	 * @param district District
	 * @param numberValue Buildings number
	 * @param bulkValue Buildings bulk
	 * @return new Buildings object created
	 */
	Buildings createBuildings(Street street, District district, String numberValue, String bulkValue) throws FlexPayException;
}
