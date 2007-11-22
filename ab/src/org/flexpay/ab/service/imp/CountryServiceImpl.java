package org.flexpay.ab.service.imp;

import org.flexpay.ab.dao.CountryDao;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.service.CountryService;

import java.util.List;

public class CountryServiceImpl implements CountryService {

	private CountryDao countryDao;

	public Country create(String name) {
		Country country = new Country();
		Long id = countryDao.create(country);

		return countryDao.read(id);
	}

	public List<Country> getCountries() {
		return countryDao.getCountries();
	}

	public void setCountryDao(CountryDao countryDao) {
		this.countryDao = countryDao;
	}
}
