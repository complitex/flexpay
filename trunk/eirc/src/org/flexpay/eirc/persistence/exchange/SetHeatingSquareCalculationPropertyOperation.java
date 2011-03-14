package org.flexpay.eirc.persistence.exchange;

import org.flexpay.payments.action.outerrequest.request.response.data.ConsumerAttributes;

import java.util.List;

/**
 *
 */
public class SetHeatingSquareCalculationPropertyOperation extends SetSquareCalculationPropertyOperation {

	public SetHeatingSquareCalculationPropertyOperation(ServiceOperationsFactory factory, List<String> datum) throws InvalidContainerException {
		super(factory, datum);
	}

	@Override
	protected String getConsumerAttributeCode() {
		return ConsumerAttributes.ATTR_HEATING_SQUARE;
	}
}
