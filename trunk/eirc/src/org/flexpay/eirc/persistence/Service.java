package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.util.Set;
import java.util.Collections;

public class Service extends DomainObject {

	private ServiceProvider serviceProvider;
	private Set<ServiceDescription> descriptions = Collections.emptySet();
	private ServiceType serviceType;

	/**
	 * Constructs a new DomainObject.
	 */
	public Service() {
	}

	public Service(Long id) {
		super(id);
	}

	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public Set<ServiceDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set<ServiceDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}
}
