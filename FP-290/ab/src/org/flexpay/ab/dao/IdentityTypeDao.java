package org.flexpay.ab.dao;

import java.util.List;

import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.common.dao.GenericDao;

public interface IdentityTypeDao extends GenericDao<IdentityType, Long> {

	List<IdentityType> listIdentityTypes(int status);
}
