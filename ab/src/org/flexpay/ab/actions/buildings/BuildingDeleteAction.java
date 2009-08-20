package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class BuildingDeleteAction extends FPActionSupport {

	private List<Long> objectIds = Collections.emptyList();
	private Long redirectBuildingsId;

	private BuildingService buildingService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		boolean wasDeleted = false;
		boolean primaryAddressError = false;
		for (Long id : objectIds) {
			Stub<BuildingAddress> addressStub = new Stub<BuildingAddress>(id);
			Building building = buildingService.findBuilding(addressStub);
			if (building == null) {
				continue;
			}
			BuildingAddress address = building.getAddress(addressStub);
			if (address == null) {
				continue;
			}

			if (address.isPrimary()) {
				primaryAddressError = true;
				continue;
			}

			address.setStatus(BuildingAddress.STATUS_DISABLED);
			buildingService.update(building);
			log.debug("Disabling address {}", address);
			wasDeleted = true;
		}

		if (wasDeleted) {
			addActionError(getText("ab.building.address_deleted"));
		} else {
			addActionError(getText("ab.building.no_address_deleted"));
		}

		if (primaryAddressError) {
			addActionError(getText("ab.building.primary_address_cannot_be_deleted"));
		}

		return redirectBuildingsId == null ? REDIRECT_SUCCESS : REDIRECT_INPUT;
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
		return REDIRECT_SUCCESS;
	}

	public void setObjectIds(List<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public Long getRedirectBuildingsId() {
		return redirectBuildingsId;
	}

	public void setRedirectBuildingsId(Long redirectBuildingsId) {
		this.redirectBuildingsId = redirectBuildingsId;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
