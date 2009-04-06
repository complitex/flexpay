package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.registry.RegistryProperties;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;

public class EircRegistryProperties extends RegistryProperties {

	private Organization sender;
	private Organization recipient;
	private ServiceProvider serviceProvider;

	/**
	 * @return the recipient stub
	 */
	public Stub<Organization> getSenderStub() {
		return new Stub<Organization>(getRegistry().getSenderCode());
	}

	/**
	 * @return the recipient stub
	 */
	public Stub<Organization> getRecipientStub() {
		return new Stub<Organization>(getRegistry().getRecipientCode());
	}

	/**
	 * Getter for property 'serviceProvider'.
	 *
	 * @return Value for property 'serviceProvider'.
	 */
	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	/**
	 * Setter for property 'serviceProvider'.
	 *
	 * @param serviceProvider Value to set for property 'serviceProvider'.
	 */
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
