package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.BuildingAttributeType;
import org.flexpay.common.dao.GenericDao;

import java.util.List;

public interface BuildingAttributeTypeDao extends GenericDao<BuildingAttributeType, Long> {

	/**
	 * Get available attribute types
	 *
	 * @return list of building attribute types
	 */
	List<BuildingAttributeType> findAttributeTypes();
}