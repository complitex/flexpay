package org.flexpay.orgs.persistence;

import org.flexpay.common.persistence.Stub;

public class ServiceProvider extends OrganizationInstance<ServiceProviderDescription, ServiceProvider> {

	private String email;

	public ServiceProvider() {
	}

	public ServiceProvider(Long id) {
		super(id);
	}

	public ServiceProvider(Stub<ServiceProvider> stub) {
		super(stub.getId());
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
