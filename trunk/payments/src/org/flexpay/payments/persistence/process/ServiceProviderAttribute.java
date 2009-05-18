package org.flexpay.payments.persistence.process;

import org.flexpay.common.persistence.Attribute;
import org.flexpay.orgs.persistence.ServiceProvider;

/**
 * Service provider attribute
 */
public class ServiceProviderAttribute extends Attribute {
    private ServiceProvider serviceProvider;

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}
