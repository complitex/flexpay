package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class ServiceOrganizationListServedBuildingsPageAction extends FPActionSupport {

	private EircServiceOrganization serviceOrganization = EircServiceOrganization.newInstance();

	private ServiceOrganizationService serviceOrganizationService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (serviceOrganization == null || serviceOrganization.isNew()) {
			log.warn("Incorrect service organization id");
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_ERROR;
		}

		Stub<EircServiceOrganization> stub = stub(serviceOrganization);
		serviceOrganization = serviceOrganizationService.read(stub);

		if (serviceOrganization == null) {
			log.warn("Can't get service organization with id {} from DB", stub.getId());
			addActionError(getText("eirc.error.service_organization.cant_get_service_organization"));
			return REDIRECT_ERROR;
		} else if (serviceOrganization.isNotActive()) {
			log.warn("Service organization with id {} is disabled", stub.getId());
			addActionError(getText("eirc.error.service_organization.cant_get_service_organization"));
			return REDIRECT_ERROR;
		}

		return SUCCESS;

	}

	@NotNull
	@Override
	protected String getErrorResult() {
		return REDIRECT_ERROR;
	}

	public EircServiceOrganization getServiceOrganization() {
		return serviceOrganization;
	}

	public void setServiceOrganization(EircServiceOrganization serviceOrganization) {
		this.serviceOrganization = serviceOrganization;
	}

	@Required
	public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
		this.serviceOrganizationService = serviceOrganizationService;
	}

}
