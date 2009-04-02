package org.flexpay.orgs.persistence.filters;

import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.ServiceProviderDescription;

import java.util.List;

public class ServiceProviderFilter
		extends OrganizationInstanceFilter<ServiceProviderDescription, ServiceProvider> {

	private List<ServiceProvider> instances;

	public ServiceProviderFilter() {
		super(-1L);
	}

	public List<ServiceProvider> getInstances() {
		return instances;
	}

	public void setInstances(List<ServiceProvider> instances) {
		this.instances = instances;
	}
}
