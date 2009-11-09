package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Country;
import org.flexpay.common.dao.GenericDao;

import java.util.List;

public interface CountryDao extends GenericDao<Country, Long> {

	/**
	 * Load all countries
	 *
	 * @param status status of desired countries
	 * @return List of countries
	 */
	List<Country> listCountries(int status);

	List<Country> findByQuery(String query, Long languageId);

	List<Country> findByNameAndLanguage(String name, Long languageId);

	List<Country> findByShortNameAndLanguage(String shortName, Long languageId);

}
