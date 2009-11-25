package org.flexpay.ab.actions.apartment;

import org.flexpay.ab.util.config.AbUserPreferences;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class ApartmentsListPageAction extends FPActionSupport {

	private Long buildingFilter;

	@NotNull
	@Override
	protected String doExecute() throws Exception {
		if (buildingFilter != null && buildingFilter > 0) {
			AbUserPreferences up = (AbUserPreferences) getUserPreferences();
			up.setBuildingFilter(buildingFilter);
			up.setApartmentFilter(0L);
		}

		return SUCCESS;
	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setBuildingFilter(Long buildingFilter) {
		this.buildingFilter = buildingFilter;
	}

}
