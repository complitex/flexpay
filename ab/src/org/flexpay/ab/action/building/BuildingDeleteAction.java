package org.flexpay.ab.action.building;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.action.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Set;

public class BuildingDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = set();

	private BuildingService buildingService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (objectIds == null) {
			log.warn("ObjectIds parameter is null");
			return SUCCESS;
		}

		List<BuildingAddress> addresses = buildingService.readFullAddresses(objectIds, false);
		Set<Long> buildingIds = set();

		for (BuildingAddress address : addresses) {
			buildingIds.add(address.getBuilding().getId());
		}

		buildingService.disable(buildingIds);

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

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
