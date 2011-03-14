package org.flexpay.eirc.action.organization;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.common.persistence.Stub.stub;

public class ServiceOrganizationAddServedBuildingPageAction extends FPActionSupport {

	private EircServiceOrganization serviceOrganization = EircServiceOrganization.newInstance();

	private ServiceOrganizationService serviceOrganizationService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

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
