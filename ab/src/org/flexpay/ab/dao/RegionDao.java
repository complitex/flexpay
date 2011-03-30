package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Region;
import org.flexpay.common.dao.NameTimeDependentDao;

import java.util.List;

public interface RegionDao extends NameTimeDependentDao<Region, Long> {

    List<Region> findWithFullHierarchyAndNames(Long regionId);

	List<Region> findByParentAndQuery(Long countryId, String query);

	List<Region> findByCountryAndNameAndLanguage(Long countryId, String name, Long languageId);

}
