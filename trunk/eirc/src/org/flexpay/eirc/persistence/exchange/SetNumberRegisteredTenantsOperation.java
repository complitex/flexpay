package org.flexpay.eirc.persistence.exchange;

import org.flexpay.payments.actions.outerrequest.request.response.data.ConsumerAttributes;

import java.util.List;

/**
 * Set number registered tenants of the house (calculation property)
 */
public class SetNumberRegisteredTenantsOperation extends SetNumberTenantsOperation {

	public SetNumberRegisteredTenantsOperation(ServiceOperationsFactory factory, List<String> datum) throws InvalidContainerException {
		super(factory, datum);
	}

	@Override
	protected String getConsumerAttributeCode() {
		return ConsumerAttributes.ATTR_NUMBER_REGISTERED_TENANTS;
	}
}
