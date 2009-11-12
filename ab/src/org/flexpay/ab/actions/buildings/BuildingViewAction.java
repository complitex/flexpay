package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class BuildingViewAction extends FPActionSupport {

	private Building building = Building.newInstance();

	private BuildingService buildingService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayException {

		if (building.isNew()) {
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		building = buildingService.readFull(stub(building));
		if (building == null) {
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
