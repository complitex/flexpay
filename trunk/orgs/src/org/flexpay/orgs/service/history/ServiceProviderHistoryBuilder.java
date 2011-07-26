package org.flexpay.orgs.service.history;

import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.ServiceProviderDescription;

public class ServiceProviderHistoryBuilder
		extends OrganizationInstanceHistoryBuilder<ServiceProviderDescription, ServiceProvider> {

    @Override
	protected ServiceProvider newInstance() {
		return new ServiceProvider();
	}

    @Override
	protected ServiceProviderDescription newDescriptionInstance() {
		return new ServiceProviderDescription();
	}
}
