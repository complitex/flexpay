package org.flexpay.orgs.service.history;

import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.persistence.ServiceOrganizationDescription;

public class ServiceOrganizationHistoryBuilder
		extends OrganizationInstanceHistoryBuilder<ServiceOrganizationDescription, ServiceOrganization> {

	protected ServiceOrganization newInstance() {
		return new ServiceOrganization();
	}

	protected ServiceOrganizationDescription newDescriptionInstance() {
		return new ServiceOrganizationDescription();
	}
}
