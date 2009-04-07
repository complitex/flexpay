package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.District;
import org.flexpay.common.dao.NameTimeDependentDao;

import java.util.List;

public interface DistrictDao extends NameTimeDependentDao<District, Long> {

	List<District> findByTownAndQuery(Long townId, String query);

}