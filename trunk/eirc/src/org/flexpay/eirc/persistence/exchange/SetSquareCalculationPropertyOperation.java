package org.flexpay.eirc.persistence.exchange;

import org.flexpay.eirc.persistence.consumer.ConsumerAttribute;

import java.util.List;

/**
 *
 */
public abstract class SetSquareCalculationPropertyOperation extends SetConsumerAttributeOperation {

	public SetSquareCalculationPropertyOperation(ServiceOperationsFactory factory, List<String> datum) throws InvalidContainerException {
		super(factory, datum);
	}

	protected boolean changed(ConsumerAttribute oldAttribute) {
		return oldAttribute == null || oldAttribute.getDoubleValue() == null || !oldAttribute.getDoubleValue().equals(getValue());
	}

	protected void setAttributeValue(ConsumerAttribute attribute) {
		attribute.setDoubleValue(getValue());
	}

	protected Double getValue() {
		return Double.parseDouble(value);
	}
}
