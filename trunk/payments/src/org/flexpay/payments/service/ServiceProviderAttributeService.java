package org.flexpay.payments.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.process.ServiceProviderAttribute;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ServiceProviderAttributeService {
    /**
	 * Find all service provider attributes
	 *
	 * @param stub Service provider
	 * @return Service providers list, empty if no service providers found
	 */
	@NotNull
    List<ServiceProviderAttribute> listServiceProviderAttributes(@NotNull Stub<ServiceProvider> stub);

    /**
	 * Save service provider attribute
	 *
	 * @param attribute New or persitent object to save
	 */
	void save(ServiceProviderAttribute attribute);
}
