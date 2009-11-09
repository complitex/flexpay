package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.District;
import org.flexpay.common.dao.NameTimeDependentDao;

import java.util.List;

public interface DistrictDao extends NameTimeDependentDao<District, Long> {

	List<District> findByParentAndQuery(Long townId, String query, Long languageId);

	List<District> findByNameAndLanguage(String name, Long languageId);

}