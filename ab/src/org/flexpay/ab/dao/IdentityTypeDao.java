package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.common.dao.GenericDao;

import java.util.List;

public interface IdentityTypeDao extends GenericDao<IdentityType, Long> {

	List<IdentityType> listIdentityTypes(int status);

	List<IdentityType> listIdentityTypesByEnumId(int typeId);

	List<IdentityType> findByNameAndLanguage(String name, Long languageId);

}
