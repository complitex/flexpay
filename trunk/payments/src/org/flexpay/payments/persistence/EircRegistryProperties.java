package org.flexpay.payments.persistence;

import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.registry.RegistryProperties;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;

public class EircRegistryProperties extends RegistryProperties {

	private Organization sender;
	private Organization recipient;
	private ServiceProvider serviceProvider;

	public Stub<Organization> getSenderStub() {
		return new Stub<Organization>(getRegistry().getSenderCode());
	}

	public Stub<Organization> getRecipientStub() {
		return new Stub<Organization>(getRegistry().getRecipientCode());
	}

	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public Organization getSender() {
		return sender;
	}

	public void setSender(Organization sender) {
		this.sender = sender;
	}

	public Organization getRecipient() {
		return recipient;
	}

	public void setRecipient(Organization recipient) {
		this.recipient = recipient;
	}

	public Stub<ServiceProvider> getServiceProviderStub() {
		return stub(getServiceProvider());
	}
}
