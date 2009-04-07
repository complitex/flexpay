package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetTypeTemporal;
import org.flexpay.common.dao.NameTimeDependentDao;

import java.util.List;

public interface StreetDao extends NameTimeDependentDao<Street, Long> {

	List<Street> findByTownAndName(Long townId, String name);

	List<Street> findByTownAndQuery(Long townId, String query);

	List<StreetTypeTemporal> findTypeTemporals(Long streetId);

	List<District> findDistricts(Long streetId);

}
