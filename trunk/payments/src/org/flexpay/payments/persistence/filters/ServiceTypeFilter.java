package org.flexpay.payments.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.payments.persistence.ServiceType;

import java.util.List;

public class ServiceTypeFilter extends PrimaryKeyFilter<ServiceType> {

	private List<ServiceType> serviceTypes;

	public ServiceTypeFilter() {
		super(-1L);
	}

    public ServiceTypeFilter(Long selectedId) {
        super(selectedId);
    }

	public List<ServiceType> getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(List<ServiceType> serviceTypes) {
		this.serviceTypes = serviceTypes;
	}
}
