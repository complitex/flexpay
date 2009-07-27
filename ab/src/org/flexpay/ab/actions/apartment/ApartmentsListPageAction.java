package org.flexpay.ab.actions.apartment;

import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class ApartmentsListPageAction extends FPActionSupport {

	private String buildingFilter;

	@Override
	@NotNull
	protected String doExecute() throws Exception {
		if (buildingFilter != null) {
			AbUserPreferences up = (AbUserPreferences) getUserPreferences();
			up.setBuildingFilterValue(buildingFilter);
			up.setApartmentFilterValue("");
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setBuildingFilter(String buildingFilter) {
		this.buildingFilter = buildingFilter;
	}

}
