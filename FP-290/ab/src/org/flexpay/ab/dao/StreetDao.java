package org.flexpay.ab.dao;

import java.util.List;

import org.flexpay.ab.persistence.Street;
import org.flexpay.common.dao.NameTimeDependentDao;

public interface StreetDao extends NameTimeDependentDao<Street, Long> {
	List<Street> findByTownAndName(Long townId, String name);
}
