package org.flexpay.eirc.actions.organization;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ServiceOrganizationDeleteAction extends FPActionSupport {

	private ServiceOrganizationService serviceOrganizationService;

	private Set<Long> objectIds = new HashSet<Long>();

	@NotNull
	public String doExecute() throws Exception {
        serviceOrganizationService.disable(objectIds);

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

    public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
        this.serviceOrganizationService = serviceOrganizationService;
    }

}
