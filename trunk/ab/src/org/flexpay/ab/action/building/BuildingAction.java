package org.flexpay.ab.action.building;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class BuildingAction extends FPActionWithPagerSupport<BuildingAddress> {

	private Long buildingFilter;
	private BuildingService buildingService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (buildingFilter == null || buildingFilter <= 0) {
			log.warn("Incorrect building id in filter ({})", buildingFilter);
			return SUCCESS;
		}

		if (buildingService.readFull(new Stub<Building>(buildingFilter)) == null) {
            log.warn("Building do not exist with id={}", buildingFilter);
            buildingFilter = 0L;
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
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

    public Long getBuildingFilter() {
        return buildingFilter;
    }

    public void setBuildingFilter(Long buildingFilter) {
        this.buildingFilter = buildingFilter;
    }

    @Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
