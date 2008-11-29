package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.eirc.persistence.ServiceOrganization;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ServiceOrganizationsListAction extends FPActionSupport {

	private ServiceOrganizationService serviceOrganizationService;

	private Page<ServiceOrganization> pager = new Page<ServiceOrganization>();
	private List<ServiceOrganization> serviceOrganizations = Collections.emptyList();

	@NotNull
	public String doExecute() throws Exception {

		serviceOrganizations = serviceOrganizationService.listServiceOrganizations(pager);

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

    public Page<ServiceOrganization> getPager() {
        return pager;
    }

    public void setPager(Page<ServiceOrganization> pager) {
        this.pager = pager;
    }

    public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
        this.serviceOrganizationService = serviceOrganizationService;
    }

}
