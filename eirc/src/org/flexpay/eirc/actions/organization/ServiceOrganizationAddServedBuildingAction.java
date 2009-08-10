package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.persistence.Building;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Set;

public class ServiceOrganizationAddServedBuildingAction extends FPActionSupport {

	private EircServiceOrganization serviceOrganization = EircServiceOrganization.newInstance();
	private Set<Long> objectIds = set();

	private BuildingService buildingService;
	private ServiceOrganizationService serviceOrganizationService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (serviceOrganization.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}

		serviceOrganization = serviceOrganizationService.read(stub(serviceOrganization));
		if (serviceOrganization == null) {
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		log.info("Served building ids: {}", objectIds);

		if (objectIds.size() > 0) {

			List<Building> buildings = buildingService.readFull(objectIds, false);
			for (Building sb : buildings) {
				ServedBuilding building = (ServedBuilding) sb;
				building.setServiceOrganization(serviceOrganization);
				serviceOrganizationService.updateServedBuilding(building);
			}
		}

		return REDIRECT_SUCCESS;
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
		return INPUT;
	}

	public EircServiceOrganization getServiceOrganization() {
		return serviceOrganization;
	}

	public void setServiceOrganization(EircServiceOrganization serviceOrganization) {
		this.serviceOrganization = serviceOrganization;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
		this.serviceOrganizationService = serviceOrganizationService;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}
}
