package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Town;
import org.flexpay.common.dao.NameTimeDependentDao;

import java.util.List;

public interface TownDao extends NameTimeDependentDao<Town, Long> {

	List<Town> findByRegionAndQuery(Long regionId, String query);

}
