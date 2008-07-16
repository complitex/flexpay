package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.Stub;

import java.util.Collections;
import java.util.List;

public class BuildingsDeleteAction extends FPActionSupport {
	private BuildingService buildingService;

	private List<Long> objectIds = Collections.emptyList();
	private Long redirectBuildingsId;

	public String execute() {
		for (Long id : objectIds) {
			Buildings buildings = buildingService.readFull(new Stub<Buildings>(id));
			buildings.setStatus(Buildings.STATUS_DISABLED);
			buildingService.update(buildings);
		}

		return redirectBuildingsId == null ? "buildings_list" : INPUT;
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
