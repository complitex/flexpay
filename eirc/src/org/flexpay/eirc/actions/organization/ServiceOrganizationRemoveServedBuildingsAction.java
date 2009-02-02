package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.persistence.ServiceOrganization;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ServiceOrganizationRemoveServedBuildingsAction extends FPActionSupport {

	private ServiceOrganizationService serviceOrganizationService;

	private Set<Long> objectIds = new HashSet<Long>();
    private ServiceOrganization serviceOrganization = new ServiceOrganization();

	@NotNull
	public String doExecute() throws Exception {

        if (serviceOrganization.getId() == null) {
            addActionError(getText("error.no_id"));
            return REDIRECT_SUCCESS;
        }

        serviceOrganization = serviceOrganizationService.read(serviceOrganization);
        if (serviceOrganization == null) {
            addActionError(getText("error.invalid_id"));
            return REDIRECT_SUCCESS;
        }

        serviceOrganizationService.removeServedBuildings(objectIds);

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
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

    public ServiceOrganization getServiceOrganization() {
        return serviceOrganization;
    }

    public void setServiceOrganization(ServiceOrganization serviceOrganization) {
        this.serviceOrganization = serviceOrganization;
    }

    public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
        this.serviceOrganizationService = serviceOrganizationService;
    }

}
