package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Town;
import org.flexpay.common.dao.NameTimeDependentDao;

import java.util.List;

public interface TownDao extends NameTimeDependentDao<Town, Long> {

	List<Town> findByParentAndQuery(Long regionId, String query);

	List<Town> findByRegionAndNameAndLanguage(Long regionId, String name, Long languageId);

}
