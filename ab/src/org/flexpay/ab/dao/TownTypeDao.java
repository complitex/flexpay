package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.common.dao.GenericDao;

import java.util.List;

public interface TownTypeDao extends GenericDao<TownType, Long> {

	List<TownType> listTownTypes(int status);
}
