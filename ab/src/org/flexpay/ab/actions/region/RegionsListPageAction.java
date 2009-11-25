package org.flexpay.ab.actions.region;

import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class RegionsListPageAction extends FPActionSupport {

	private Long countryFilter;

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		if (countryFilter != null && countryFilter > 0) {
			AbUserPreferences up = (AbUserPreferences) getUserPreferences();
			up.setCountryFilter(countryFilter);
			up.setRegionFilter(0L);
			up.setTownFilter(0L);
			up.setDistrictFilter(0L);
			up.setStreetFilter(0L);
			up.setBuildingFilter(0L);
			up.setApartmentFilter(0L);
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setCountryFilter(Long countryFilter) {
		this.countryFilter = countryFilter;
	}

}
