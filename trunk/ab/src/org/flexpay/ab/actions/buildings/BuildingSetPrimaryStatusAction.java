package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.AddressService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class BuildingSetPrimaryStatusAction extends FPActionSupport {

	private BuildingAddress buildings = new BuildingAddress();
	private Long redirectBuildingsId;

	private BuildingService buildingService;

	@NotNull
	public String doExecute() throws Exception {

		Stub<BuildingAddress> addressStub = stub(buildings);
		Building building = buildingService.findBuilding(addressStub);
		if (building == null) {
			return REDIRECT_SUCCESS;
		}
		building.setPrimaryAddress(addressStub);
		buildingService.update(building);

		addActionError(getText("ab.building.primary_address_set"));

		return REDIRECT_SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
	}

	public BuildingAddress getBuildings() {
		return buildings;
	}

	public void setBuildings(BuildingAddress buildingAddress) {
		this.buildings = buildingAddress;
	}

	public Long getRedirectBuildingsId() {
		return redirectBuildingsId;
	}

	public void setRedirectBuildingsId(Long redirectBuildingsId) {
		this.redirectBuildingsId = redirectBuildingsId;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}
}
