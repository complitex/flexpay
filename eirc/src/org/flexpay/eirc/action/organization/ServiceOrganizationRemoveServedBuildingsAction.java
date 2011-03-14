package org.flexpay.eirc.action.organization;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public class ServiceOrganizationRemoveServedBuildingsAction extends FPActionSupport {

	private Set<Long> objectIds = set();

	private ServiceOrganizationService serviceOrganizationService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		serviceOrganizationService.removeServedBuildings(objectIds);

		addActionMessage(getText("eirc.service_organization.served_building_removed"));

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

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	@Required
	public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
		this.serviceOrganizationService = serviceOrganizationService;
	}

}
