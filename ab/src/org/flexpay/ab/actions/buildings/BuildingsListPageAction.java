package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class BuildingsListPageAction extends FPActionSupport {

	private String streetFilter;

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		if (streetFilter != null) {
			AbUserPreferences up = (AbUserPreferences) getUserPreferences();
			up.setStreetFilterValue(streetFilter);
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

	public void setStreetFilter(String streetFilter) {
		this.streetFilter = streetFilter;
	}

}
