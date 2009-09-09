package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.Attribute;

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
