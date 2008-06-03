package org.flexpay.eirc.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.eirc.persistence.ServiceType;

import java.util.List;

public class ServiceTypeFilter extends PrimaryKeyFilter {

	private List<ServiceType> serviceTypes;

	public ServiceTypeFilter() {
		super(-1L);
	}

	public List<ServiceType> getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(List<ServiceType> serviceTypes) {
		this.serviceTypes = serviceTypes;
	}
}