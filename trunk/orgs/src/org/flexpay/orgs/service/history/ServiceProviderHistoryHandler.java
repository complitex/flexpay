package org.flexpay.orgs.service.history;

import org.flexpay.common.persistence.history.Diff;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.ServiceProviderDescription;
import org.jetbrains.annotations.NotNull;

public class ServiceProviderHistoryHandler
		extends OrganizationInstanceHistoryHandler<ServiceProviderDescription, ServiceProvider> {

	protected ServiceProvider newInstance() {
		return new ServiceProvider();
	}

	protected Class<ServiceProvider> getType() {
		return ServiceProvider.class;
	}

	/**
	 * Check if this handler can handle this <code>diff</code>
	 *
	 * @param diff Diff that is to be handled
	 * @return <code>
	 */
	public boolean supports(@NotNull Diff diff) {
		return typeRegistry.getType(ServiceProvider.class) == diff.getObjectType();
	}
}
