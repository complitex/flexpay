package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.common.dao.GenericDao;

import java.util.List;

public interface AddressAttributeTypeDao extends GenericDao<AddressAttributeType, Long> {

	/**
	 * Get available attribute types
	 *
	 * @return list of building attribute types
	 */
	List<AddressAttributeType> findAttributeTypes();

	List<AddressAttributeType> findByNameAndLanguage(String name, Long languageId);

	List<AddressAttributeType> findByShortNameAndLanguage(String shortName, Long languageId);

}