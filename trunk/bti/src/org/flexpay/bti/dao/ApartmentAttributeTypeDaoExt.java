package org.flexpay.bti.dao;

import org.flexpay.bti.persistence.apartment.ApartmentAttributeType;

public interface ApartmentAttributeTypeDaoExt {

	ApartmentAttributeType readFull(Long id);

	/**
	 * Check if there is only
	 *
	 * @param name Translation to check
	 * @param typeId Type key
	 * @return <code>true</code> if this name is unique, or <code>false</code> otherwise
	 */
	boolean isUniqueTypeName(String name, Long typeId);

}