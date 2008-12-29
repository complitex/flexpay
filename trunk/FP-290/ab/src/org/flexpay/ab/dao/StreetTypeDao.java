package org.flexpay.ab.dao;

import java.util.List;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.common.dao.GenericDao;
import org.jetbrains.annotations.NotNull;

public interface StreetTypeDao extends GenericDao<StreetType, Long> {

	@NotNull
	List<StreetType> listStreetTypes(int status);
}