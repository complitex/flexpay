package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class BuildingDeleteAction extends FPActionSupport {

	private BuildingService buildingService;

	private List<Long> objectIds = Collections.emptyList();
	private Long redirectBuildingsId;

	@NotNull
	public String doExecute() {
		for (Long id : objectIds) {
			Buildings buildings = buildingService.readFull(new Stub<Buildings>(id));
			if (buildings == null) {
				continue;
			}
			buildings.setStatus(Buildings.STATUS_DISABLED);
			buildingService.update(buildings);
		}

		return redirectBuildingsId == null ? REDIRECT_SUCCESS : REDIRECT_INPUT;
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
	 * @param buildingService the buildingService to set
	 */
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	/**
	 * @param objectIds the objectIds to set
	 */
	public void setObjectIds(List<Long> objectIds) {
		this.objectIds = objectIds;
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
