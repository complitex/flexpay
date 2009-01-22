package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class BuildingSetPrimaryStatusAction extends FPActionSupport {

	private BuildingService buildingService;

	private BuildingAddress buildings;
	private Long redirectBuildingsId;

	@NotNull
	public String doExecute() throws FlexPayException {
		buildings = buildingService.readFull(stub(buildings));
		for (BuildingAddress current : buildingService.getBuildingBuildings(buildings.getBuildingStub())) {
			current.setPrimaryStatus(buildings.equals(current));
			buildingService.update(current);
		}

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

	/**
	 * @param buildingService
	 *            the buildingService to set
	 */
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	/**
	 * @return the buildings
	 */
	public BuildingAddress getBuildings() {
		return buildings;
	}

	/**
	 * @param buildingAddress
	 *            the buildings to set
	 */
	public void setBuildings(BuildingAddress buildingAddress) {
		this.buildings = buildingAddress;
	}

	/**
	 * @return the redirectBuildingsId
	 */
	public Long getRedirectBuildingsId() {
		return redirectBuildingsId;
	}

	/**
	 * @param redirectBuildingsId the redirectBuildingsId to set
	 */
	public void setRedirectBuildingsId(Long redirectBuildingsId) {
		this.redirectBuildingsId = redirectBuildingsId;
	}

}
