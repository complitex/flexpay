package org.flexpay.eirc.persistence.exchange;

import org.flexpay.eirc.persistence.consumer.ConsumerAttribute;
import org.flexpay.payments.action.outerrequest.request.response.data.ConsumerAttributes;

import java.util.List;

/**
 * Set number tenants of the house (calculation property)
 */
public class SetNumberTenantsOperation extends SetConsumerAttributeOperation {
	
	public SetNumberTenantsOperation(ServiceOperationsFactory factory, List<String> datum) throws InvalidContainerException {
		super(factory, datum);
	}

    @Override
	protected boolean changed(ConsumerAttribute oldAttribute) {
		return oldAttribute == null || oldAttribute.getIntValue() == null || !oldAttribute.getIntValue().equals(getValue());
	}

    @Override
	protected void setAttributeValue(ConsumerAttribute attribute) {
		attribute.setIntValue(getValue());
	}

	protected Integer getValue() {
		return Integer.parseInt(value);
	}

	@Override
	protected String getConsumerAttributeCode() {
		return ConsumerAttributes.ATTR_NUMBER_TENANTS;
	}
}
