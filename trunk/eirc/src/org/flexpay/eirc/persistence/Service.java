package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;

public class Service extends DomainObject {

	private ServiceProvider serviceProvider;
	private String description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}
}
