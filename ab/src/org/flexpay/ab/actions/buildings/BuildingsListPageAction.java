package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class BuildingsListPageAction extends FPActionSupport {

	private Long streetFilter;

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		if (streetFilter != null && streetFilter > 0) {
			AbUserPreferences up = (AbUserPreferences) getUserPreferences();
			up.setStreetFilter(streetFilter);
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

	public void setStreetFilter(Long streetFilter) {
		this.streetFilter = streetFilter;
	}

}
