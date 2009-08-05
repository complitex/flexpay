package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class BuildingsListAjaxAction extends FPActionWithPagerSupport<BuildingAddress> {

	private String streetId;
	private List<BuildingAddress> buildings = list();

	private BuildingService buildingService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		Long streetIdLong;

		try {
			streetIdLong = Long.parseLong(streetId);
		} catch (Exception e) {
			log.warn("Incorrect street id in filter ({})", streetId);
			return SUCCESS;
		}

		buildings = buildingService.getBuildings(new Stub<Street>(streetIdLong), getPager());
		log.info("Total buildings found: {}", buildings);

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
		return SUCCESS;
	}

	public void setStreetId(String streetId) {
		this.streetId = streetId;
	}

	public List<BuildingAddress> getBuildings() {
		return buildings;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
