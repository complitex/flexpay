package org.flexpay.ab.actions.town;

import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class TownsListPageAction extends FPActionSupport {

	private Long regionFilter;

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		if (regionFilter != null && regionFilter > 0) {
			AbUserPreferences up = (AbUserPreferences) getUserPreferences();
			up.setRegionFilter(regionFilter);
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

	public void setRegionFilter(Long regionFilter) {
		this.regionFilter = regionFilter;
	}

}
