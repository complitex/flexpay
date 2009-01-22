package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.common.dao.GenericDao;

import java.util.List;

public interface BuildingAttributeTypeDao extends GenericDao<AddressAttributeType, Long> {

	/**
	 * Get available attribute types
	 *
	 * @return list of building attribute types
	 */
	List<AddressAttributeType> findAttributeTypes();
}