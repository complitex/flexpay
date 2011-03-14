package org.flexpay.eirc.action.organization;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.set;

public class ServiceOrganizationAddServedBuildingsAction extends FPActionSupport {

	private EircServiceOrganization serviceOrganization = EircServiceOrganization.newInstance();
	private Set<Long> objectIds = set();

	private ServiceOrganizationService serviceOrganizationService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (objectIds == null || objectIds.isEmpty()) {
			return SUCCESS;
		}

		if (serviceOrganization == null || serviceOrganization.isNew()) {
			log.warn("Incorrect service organization id");
			addActionError(getText("common.error.invalid_id"));
			return SUCCESS;
		}

		Stub<EircServiceOrganization> stub = stub(serviceOrganization);
		serviceOrganization = serviceOrganizationService.read(stub);

		if (serviceOrganization == null) {
			log.warn("Can't get service organization with id {} from DB", stub.getId());
			addActionError(getText("eirc.error.service_organization.cant_get_service_organization"));
			return SUCCESS;
		} else if (serviceOrganization.isNotActive()) {
			log.warn("Service organization with id {} is disabled", stub.getId());
			addActionError(getText("eirc.error.service_organization.cant_get_service_organization"));
			return SUCCESS;
		}

		serviceOrganizationService.addServedBuildings(objectIds, serviceOrganization);

		addActionMessage(getText("eirc.service_organization.served_building_added"));

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

	public void setServiceOrganization(EircServiceOrganization serviceOrganization) {
		this.serviceOrganization = serviceOrganization;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
		this.serviceOrganizationService = serviceOrganizationService;
	}

}
