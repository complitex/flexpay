package org.flexpay.eirc.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.eirc.persistence.ServiceProvider;

import java.util.List;

public class ServiceProviderFilter extends PrimaryKeyFilter {

	private List<ServiceProvider> providers;

	public ServiceProviderFilter() {
		super(-1L);
	}

	public List<ServiceProvider> getProviders() {
		return providers;
	}

	public void setProviders(List<ServiceProvider> providers) {
		this.providers = providers;
	}
}
