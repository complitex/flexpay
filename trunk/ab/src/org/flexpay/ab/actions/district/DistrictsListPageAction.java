package org.flexpay.ab.actions.district;

import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class DistrictsListPageAction extends FPActionSupport {

	private Long townFilter;

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		if (townFilter != null && townFilter > 0) {
			AbUserPreferences up = (AbUserPreferences) getUserPreferences();
			up.setTownFilter(townFilter);
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

	public void setTownFilter(Long townFilter) {
		this.townFilter = townFilter;
	}

}
