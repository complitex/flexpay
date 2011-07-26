package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.history.Diff;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.persistence.ServiceOrganizationDescription;
import org.flexpay.orgs.service.OrgsObjectsFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class ServiceOrganizationHistoryHandler
		extends OrganizationInstanceHistoryHandler<ServiceOrganizationDescription, ServiceOrganization> {

	private OrgsObjectsFactory objectsFactory;

    @Override
	protected ServiceOrganization newInstance() {
		return objectsFactory.newServiceOrganization();
	}

    @Override
	protected Class<ServiceOrganization> getType() {
		return ServiceOrganization.class;
	}

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
    @Override
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(ServiceOrganization.class) == diff.getObjectType();
	}

	@Required
	public void setObjectsFactory(OrgsObjectsFactory objectsFactory) {
		this.objectsFactory = objectsFactory;
	}
}
