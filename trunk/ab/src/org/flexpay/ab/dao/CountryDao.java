package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.Country;
import org.flexpay.common.dao.GenericDao;

import java.util.List;

public interface CountryDao extends GenericDao<Country, Long> {

	List<Country> getCountries();
}
