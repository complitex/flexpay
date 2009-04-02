package org.flexpay.orgs.actions.organization;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.List;

public class ServiceOrganizationsListAction extends FPActionWithPagerSupport<ServiceOrganization> {

	private List<ServiceOrganization> serviceOrganizations = Collections.emptyList();

	private ServiceOrganizationService serviceOrganizationService;

	@NotNull
	public String doExecute() throws Exception {

		serviceOrganizations = serviceOrganizationService.listServiceOrganizations(getPager());

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

	public List<ServiceOrganization> getServiceOrganizations() {
		return serviceOrganizations;
	}

	@Required
	public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
		this.serviceOrganizationService = serviceOrganizationService;
	}
}
