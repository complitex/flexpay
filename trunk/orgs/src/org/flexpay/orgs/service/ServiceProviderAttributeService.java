package org.flexpay.orgs.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.ServiceProviderAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        * Find service provider attribute by name for service provider
        *
        * @param stub Service provider
        * @param attributeName Attribute name
	 * @return Service providers l attribute mpty if no service providers found
        */
    @Nullable
    ServiceProviderAttribute getServiceProviderAttribute(@NotNull Stub<ServiceProvider>stub, @NotNull String attributeName);

    /**
	 * Save service provider attribute
	 *
	 * @param attribute New or persitent object to save
	 */
	void save(@NotNull ServiceProviderAttribute attribute);
}
