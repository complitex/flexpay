package org.flexpay.ab.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.filters.BuildingsFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.exception.FlexPayException;

import java.util.List;

public interface BuildingService extends ParentService<BuildingsFilter> {

	public List<Buildings> getBuildings(ArrayStack filters, Page pager);

	/**
	 * Get building attribute type
	 *
	 * @param type Attribute type
	 * @return BuildingAttributeType
	 * @throws FlexPayException if failure occurs
	 */
	public BuildingAttributeType getAttributeType(int type) throws FlexPayException;

	/**
	 * Find building by number
	 *
	 * @param street Building street
	 * @param district Building district
	 * @param number Building number
	 * @param bulk Building bulk number
	 * @return Buildings instance, or <code>null</null> if not found
	 */
	Buildings findBuildings(Street street, District district, String number, String bulk);
}
