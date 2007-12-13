package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Region;
import org.flexpay.common.dao.GenericDao;

import java.util.List;

public interface RegionDao extends GenericDao<Region, Long> {

	List<Region> listRegions(int status, Long countryId);
}
