package org.flexpay.ab.dao;

import java.util.List;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.common.dao.GenericDao;

public interface StreetTypeDao extends GenericDao<StreetType, Long> {

	List<StreetType> listStreetTypes(int status);
}