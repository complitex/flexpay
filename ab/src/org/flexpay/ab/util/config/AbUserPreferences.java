package org.flexpay.ab.util.config;

import org.flexpay.common.util.config.UserPreferences;

public class AbUserPreferences extends UserPreferences {

	protected AbUserPreferences() {
		
	}

	private String countryFilterValue;
	private String regionFilterValue;
	private String townFilterValue;
	private String districtFilterValue;
	private String streetFilterValue;
	private String buildingFilterValue;
	private String apartmentFilterValue;

	public String getCountryFilterValue() {
		return countryFilterValue;
	}

	public void setCountryFilterValue(String countryFilterValue) {
		this.countryFilterValue = countryFilterValue;
	}

	public String getRegionFilterValue() {
		return regionFilterValue;
	}

	public void setRegionFilterValue(String regionFilterValue) {
		this.regionFilterValue = regionFilterValue;
	}

	public String getTownFilterValue() {
		return townFilterValue;
	}

	public void setTownFilterValue(String townFilterValue) {
		this.townFilterValue = townFilterValue;
	}

	public String getDistrictFilterValue() {
		return districtFilterValue;
	}

	public void setDistrictFilterValue(String districtFilterValue) {
		this.districtFilterValue = districtFilterValue;
	}

	public String getStreetFilterValue() {
		return streetFilterValue;
	}

	public void setStreetFilterValue(String streetFilterValue) {
		this.streetFilterValue = streetFilterValue;
	}

	public String getBuildingFilterValue() {
		return buildingFilterValue;
	}

	public void setBuildingFilterValue(String buildingFilterValue) {
		this.buildingFilterValue = buildingFilterValue;
	}

	public String getApartmentFilterValue() {
		return apartmentFilterValue;
	}

	public void setApartmentFilterValue(String apartmentFilterValue) {
		this.apartmentFilterValue = apartmentFilterValue;
	}
}
