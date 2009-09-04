package org.flexpay.bti.dao;

import org.flexpay.bti.persistence.building.BuildingAttributeType;

import java.util.List;

public interface BuildingAttributeTypeDaoExt {

	BuildingAttributeType readFull(Long id);

	List<BuildingAttributeType> readFullAll();

	/**
	 * Check if there is only
	 * 
	 * @param name Translation to check
	 * @param typeId Type key
	 * @return <code>true</code> if this name is unique, or <code>false</code> otherwise
	 */
	boolean isUniqueTypeName(String name, Long typeId);

}
