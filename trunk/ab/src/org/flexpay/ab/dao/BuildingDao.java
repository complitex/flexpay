package org.flexpay.ab.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.ab.persistence.Building;

import java.util.List;

public interface BuildingDao extends GenericDao<Building, Long> {

	List<Building> findStreetBuildings(Long streetId);
}
