package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Region;
import org.flexpay.common.dao.NameTimeDependentDao;

import java.util.List;

public interface RegionDao extends NameTimeDependentDao<Region, Long> {

	List<Region> findByCountryAndQuery(Long countryId, String query);

}
