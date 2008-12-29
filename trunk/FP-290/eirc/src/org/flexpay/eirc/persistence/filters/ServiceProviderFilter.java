package org.flexpay.eirc.persistence.filters;

import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.persistence.ServiceProviderDescription;

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
