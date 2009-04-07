package org.flexpay.ab.actions.filters;

import org.flexpay.ab.actions.buildings.BuildingsActionsBase;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class BuildingsListAjaxAction extends BuildingsActionsBase {

	private String parent;
	private List<BuildingAddress> buildingsList = list();

	private BuildingService buildingService;

	@NotNull
	public String doExecute() throws Exception {

		Long streetIdLong;

		try {
			streetIdLong = Long.parseLong(parent);
		} catch (Exception e) {
			log.warn("Incorrect street id in filter ({})", parent);
			return SUCCESS;
		}

		buildingsList = buildingService.getBuildings(streetIdLong, getPager());

		return SUCCESS;
	}

	protected void setBreadCrumbs() {
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
		return SUCCESS;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public List<BuildingAddress> getBuildingsList() {
		return buildingsList;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
