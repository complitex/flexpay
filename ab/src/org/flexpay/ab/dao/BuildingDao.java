package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Building;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.FetchRange;

import java.util.List;

public interface BuildingDao extends GenericDao<Building, Long> {

	List<Building> findSimpleByTown(Long townId, FetchRange range);
}
