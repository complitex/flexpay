package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashSet;
import java.util.Set;

public class ServiceOrganizationRemoveServedBuildingsAction extends FPActionSupport {

	private Set<Long> objectIds = set();
    private EircServiceOrganization serviceOrganization = EircServiceOrganization.newInstance();

	private ServiceOrganizationService serviceOrganizationService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

        if (serviceOrganization.getId() == null) {
            addActionError(getText("error.no_id"));
            return REDIRECT_SUCCESS;
        }

        serviceOrganization = (EircServiceOrganization) serviceOrganizationService.read(stub(serviceOrganization));
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
	@Override
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
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
