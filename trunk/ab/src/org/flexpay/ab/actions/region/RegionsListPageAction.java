package org.flexpay.ab.actions.region;

import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class RegionsListPageAction extends FPActionSupport {

	private String countryFilter;

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		if (countryFilter != null) {
			AbUserPreferences up = (AbUserPreferences) getUserPreferences();
			up.setCountryFilterValue(countryFilter);
			up.setRegionFilterValue("");
			up.setTownFilterValue("");
			up.setDistrictFilterValue("");
			up.setStreetFilterValue("");
			up.setBuildingFilterValue("");
			up.setApartmentFilterValue("");
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setCountryFilter(String countryFilter) {
		this.countryFilter = countryFilter;
	}

}
