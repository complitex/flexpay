package org.flexpay.eirc.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class BuildingAjaxAction {

	private Long streetId;
	private List<BuildingAddress> buildingsList;

	private BuildingService buildingService;

	public String execute() throws FlexPayException {
		buildingsList = getBuildingListByStreetId(streetId);

		return ActionSupport.SUCCESS;
	}

	List<BuildingAddress> getBuildingListByStreetId(Long streetId) {
		Page<?> pager = new Page(200, 1);
		List<BuildingAddress> buildingses = null;
		while (true) {
			if (buildingses == null) {
				buildingses = buildingService.getBuildings(streetId, pager);
			} else {
				buildingses.addAll(buildingService.getBuildings(streetId, pager));
			}
			if (pager.hasNextPage()) {
				pager.setPageNumber(pager.getNextPageNumber());
			} else {
				break;
			}
		}

		return buildingses;
	}

	public void setStreetId(Long streetId) {
		this.streetId = streetId;
	}

	public List<BuildingAddress> getBuildingsList() {
		return buildingsList;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
