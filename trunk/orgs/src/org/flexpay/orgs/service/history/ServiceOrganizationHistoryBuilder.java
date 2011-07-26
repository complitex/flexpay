package org.flexpay.orgs.service.history;

import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.persistence.ServiceOrganizationDescription;
import org.flexpay.orgs.service.OrgsObjectsFactory;
import org.springframework.beans.factory.annotation.Required;

public class ServiceOrganizationHistoryBuilder
		extends OrganizationInstanceHistoryBuilder<ServiceOrganizationDescription, ServiceOrganization> {

	private OrgsObjectsFactory objectsFactory;

    @Override
	protected ServiceOrganization newInstance() {
		return objectsFactory.newServiceOrganization();
	}

    @Override
	protected ServiceOrganizationDescription newDescriptionInstance() {
		return new ServiceOrganizationDescription();
	}

	@Required
	public void setObjectsFactory(OrgsObjectsFactory objectsFactory) {
		this.objectsFactory = objectsFactory;
	}
}
