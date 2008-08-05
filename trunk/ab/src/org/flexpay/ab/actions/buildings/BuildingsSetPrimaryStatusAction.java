package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.actions.FPActionSupport;
import org.jetbrains.annotations.NotNull;

public class BuildingsSetPrimaryStatusAction extends FPActionSupport {

	private BuildingService buildingService;

	private Buildings buildings;
	private Long redirectBuildingsId;

	@NotNull
	public String doExecute() throws FlexPayException {
		buildings = buildingService.readFull(stub(buildings));
		for (Buildings current : buildingService.getBuildingBuildings(buildings.getBuilding())) {
			current.setPrimaryStatus(buildings.getId().longValue() == current.getId().longValue());
			buildingService.update(current);
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
	protected String getErrorResult() {
		return SUCCESS;
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
	public Buildings getBuildings() {
		return buildings;
	}

	/**
	 * @param buildings
	 *            the buildings to set
	 */
	public void setBuildings(Buildings buildings) {
		this.buildings = buildings;
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
