package org.flexpay.ab.actions;

import org.flexpay.ab.service.CountryService;

public class CountryCreate {
	private String name;

	private CountryService countryService;

	public void execute() {

	}

	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}
}
