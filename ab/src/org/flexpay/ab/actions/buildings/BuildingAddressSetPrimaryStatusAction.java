package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class BuildingAddressSetPrimaryStatusAction extends FPActionSupport {

	private BuildingAddress address = new BuildingAddress();

	private BuildingService buildingService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		Stub<BuildingAddress> addressStub = stub(address);
		Building building = buildingService.findBuilding(addressStub);
		if (building == null) {
			log.warn("Can't find building for address with id {}", addressStub.getId());
			return SUCCESS;
		}
		building.setPrimaryAddress(addressStub);
		buildingService.update(building);

		addActionMessage(getText("ab.building.primary_address_set"));

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

	public void setAddress(BuildingAddress address) {
		this.address = address;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
