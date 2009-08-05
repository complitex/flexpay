package org.flexpay.ab.actions.street;

import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class StreetsListPageAction extends FPActionSupport {

	private String townFilter;

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		if (townFilter != null) {
			AbUserPreferences up = (AbUserPreferences) getUserPreferences();
			up.setTownFilterValue(townFilter);
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

	public void setTownFilter(String townFilter) {
		this.townFilter = townFilter;
	}

}
