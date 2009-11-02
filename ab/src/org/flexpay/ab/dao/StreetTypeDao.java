package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.common.dao.GenericDao;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface StreetTypeDao extends GenericDao<StreetType, Long> {

	@NotNull
	List<StreetType> listStreetTypes(int status);

	List<StreetType> findByNameAndLanguage(String name, Long languageId, int status);

	List<StreetType> findByShortNameAndLanguage(String shortName, Long languageId, int status);

}
