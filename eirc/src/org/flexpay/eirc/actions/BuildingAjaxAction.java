package org.flexpay.eirc.actions;

import java.util.List;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import com.opensymphony.xwork2.ActionSupport;

public class BuildingAjaxAction {

	private BuildingService buildingService;

	private Long streetId;
	private List<BuildingAddress> buildingsList;

	public String execute() throws FlexPayException {
		buildingsList = getBuildingListByStreetId(streetId);

		return ActionSupport.SUCCESS;
	}

	List<BuildingAddress> getBuildingListByStreetId(Long streetId) {
		Page pager = new Page(200, 1);
		List<BuildingAddress> buildingses = null;
		while (true) {
			if (buildingses == null) {
				buildingses = buildingService.getBuildings(streetId, pager);
			} else {
				buildingses.addAll(buildingService.getBuildings(streetId,
						pager));
			}
			if (pager.hasNextPage()) {
				pager.setPageNumber(pager.getNextPageNumber());
			} else {
				break;
			}
		}

		return buildingses;
	}

	/**
	 * @param streetId
	 *            the streetId to set
	 */
	public void setStreetId(Long streetId) {
		this.streetId = streetId;
	}

	/**
	 * @param buildingService
	 *            the buildingService to set
	 */
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	/**
	 * @return the buildingsList
	 */
	public List<BuildingAddress> getBuildingsList() {
		return buildingsList;
	}

}
