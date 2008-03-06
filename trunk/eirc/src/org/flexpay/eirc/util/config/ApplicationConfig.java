package org.flexpay.eirc.util.config;

import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.Country;
import org.flexpay.ab.persistence.Region;

public class ApplicationConfig extends org.flexpay.common.util.config.ApplicationConfig {

	private Country defaultCountry;
	private Region defaultRegion;
	private Town defaultTown;

	public Town getDefaultTown() {
		return defaultTown;
	}

	public Country getDefaultCountry() {
		return defaultCountry;
	}

	public Region getDefaultRegion() {
		return defaultRegion;
	}

	/**
	 * TODO: perform lookup by name, not id
	 * @param townId Town id
	 */
	public void setDefaultTownId(String townId) {
		defaultTown = new Town(Long.valueOf(townId));
	}

	/**
	 * TODO: perform lookup by name, not id
	 * @param regionId Region id
	 */
	public void setDefaultRegionId(String regionId) {
		defaultRegion = new Region(Long.valueOf(regionId));
	}

	/**
	 * TODO: perform lookup by name, not id
	 * @param countryId Country id
	 */
	public void setDefaultCountryId(String countryId) {
		defaultCountry = new Country(Long.valueOf(countryId));
	}

	public static ApplicationConfig getInstance() {
		return (ApplicationConfig) org.flexpay.common.util.config.ApplicationConfig.getInstance();
	}
}
