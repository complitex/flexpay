package org.flexpay.ab.action.building;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.action.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class BuildingAddressDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = set();
	private Building building = Building.newInstance();

	private BuildingService buildingService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (building == null || building.isNew()) {
			log.warn("Incorrect building id");
			addActionError("ab.error.building.incorrect_building_id");
			return SUCCESS;
		}

		if (objectIds == null) {
			log.warn("ObjectIds parameter is null");
			return SUCCESS;
		}

		buildingService.disableAddresses(objectIds, stub(building));

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

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
