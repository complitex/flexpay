package org.flexpay.eirc.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.eirc.persistence.Service;

import java.util.List;

public class ServiceFilter extends PrimaryKeyFilter {

	private List<Service> services;

	public ServiceFilter() {
		super(-1L);
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}
}