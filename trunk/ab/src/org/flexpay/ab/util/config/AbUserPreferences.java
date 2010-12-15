package org.flexpay.ab.util.config;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.util.config.UserPreferences;

public class AbUserPreferences extends UserPreferences {

	private Long countryFilter = 0L;
	private Long regionFilter = 0L;
	private Long townFilter = 0L;
	private Long districtFilter = 0L;
	private Long streetFilter = 0L;
	private Long buildingFilter = 0L;
	private Long apartmentFilter = 0L;

	public AbUserPreferences() {
	}

	public Long getCountryFilter() {
		return countryFilter;
	}

	public void setCountryFilter(Long countryFilter) {
		this.countryFilter = countryFilter;           
	}

	public Long getRegionFilter() {
		return regionFilter;
	}

	public void setRegionFilter(Long regionFilter) {
		this.regionFilter = regionFilter;
	}

	public Long getTownFilter() {
		return townFilter;
	}

	public void setTownFilter(Long townFilter) {
		this.townFilter = townFilter;
	}

	public Long getDistrictFilter() {
		return districtFilter;
	}

	public void setDistrictFilter(Long districtFilter) {
		this.districtFilter = districtFilter;
	}

	public Long getStreetFilter() {
		return streetFilter;
	}

	public void setStreetFilter(Long streetFilter) {
		this.streetFilter = streetFilter;
	}

	public Long getBuildingFilter() {
		return buildingFilter;
	}

	public void setBuildingFilter(Long buildingFilter) {
		this.buildingFilter = buildingFilter;
	}

	public Long getApartmentFilter() {
		return apartmentFilter;
	}

	public void setApartmentFilter(Long apartmentFilter) {
		this.apartmentFilter = apartmentFilter;
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                appendSuper(super.toString()).
                append("countryFilter", countryFilter).
                append("regionFilter", regionFilter).
                append("townFilter", townFilter).
                append("districtFilter", districtFilter).
                append("streetFilter", streetFilter).
                append("buildingFilter", buildingFilter).
                append("apartmentFilter", apartmentFilter).
                toString();
    }
}
