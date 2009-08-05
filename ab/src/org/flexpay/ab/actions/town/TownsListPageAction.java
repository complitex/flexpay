package org.flexpay.ab.actions.town;

import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class TownsListPageAction extends FPActionSupport {

	private String regionFilter;

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		if (regionFilter != null) {
			AbUserPreferences up = (AbUserPreferences) getUserPreferences();
			up.setRegionFilterValue(regionFilter);
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

	public void setRegionFilter(String regionFilter) {
		this.regionFilter = regionFilter;
	}

}
