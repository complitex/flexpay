package org.flexpay.ab.actions.buildings;

import java.util.Collections;
import java.util.List;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.service.BuildingService;

public class BuildingsDeleteAction extends CommonAction {
	private BuildingService buildingService;

	private List<Long> objectIds = Collections.emptyList();

	public String execute() {
		for (Long id : objectIds) {
			Buildings buildings = buildingService.readFull(id);
			buildings.setStatus(Buildings.STATUS_DISABLED);
			buildingService.update(buildings);
		}

		return "success";
	}

	/**
	 * @param buildingService
	 *            the buildingService to set
	 */
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	/**
	 * @param objectIds
	 *            the objectIds to set
	 */
	public void setObjectIds(List<Long> objectIds) {
		this.objectIds = objectIds;
	}

}
