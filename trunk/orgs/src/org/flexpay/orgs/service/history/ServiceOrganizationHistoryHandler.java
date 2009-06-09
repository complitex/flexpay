package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.history.Diff;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.persistence.ServiceOrganizationDescription;
import org.jetbrains.annotations.NotNull;

public class ServiceOrganizationHistoryHandler
		extends OrganizationInstanceHistoryHandler<ServiceOrganizationDescription, ServiceOrganization> {

	protected ServiceOrganization newInstance() {
		return new ServiceOrganization();
	}

	protected Class<ServiceOrganization> getType() {
		return ServiceOrganization.class;
	}

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(ServiceOrganization.class) == diff.getObjectType();
	}
}
