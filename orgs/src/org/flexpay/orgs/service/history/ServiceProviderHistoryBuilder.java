package org.flexpay.orgs.service.history;

import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.ServiceProviderDescription;

public class ServiceProviderHistoryBuilder
		extends OrganizationInstanceHistoryBuilder<ServiceProviderDescription, ServiceProvider> {

	protected ServiceProvider newInstance() {
		return new ServiceProvider();
	}

	protected ServiceProviderDescription newDescriptionInstance() {
		return new ServiceProviderDescription();
	}
}
