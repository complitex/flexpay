package org.flexpay.ab.service;

import org.flexpay.ab.persistence.Country;

import java.util.List;

public interface CountryService {

	Country create(String name);

	List<Country> getCountries();
}
