package org.flexpay.ab.action.building;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class BuildingViewAction extends FPActionSupport {

	private Building building = Building.newInstance();

	private BuildingService buildingService;

	@NotNull
	@Override
	public String doExecute() throws FlexPayException {

		if (building == null || building.isNew()) {
			log.warn("Incorrect building id");
			addActionError(getText("ab.error.building.incorrect_building_id"));
			return REDIRECT_ERROR;
		}

		Stub<Building> stub = stub(building);
		building = buildingService.readFull(stub);

		if (building == null) {
			log.warn("Can't get building with id {} from DB", stub.getId());
			addActionError(getText("ab.error.building.cant_get_building"));
			return REDIRECT_ERROR;
		} else if (building.isNotActive()) {
			log.warn("Country with id {} is disabled", stub.getId());
			addActionError(getText("ab.error.building.cant_get_building"));
			return REDIRECT_ERROR;
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
		return REDIRECT_ERROR;
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
