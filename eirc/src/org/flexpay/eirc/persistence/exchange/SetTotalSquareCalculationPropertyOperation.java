package org.flexpay.eirc.persistence.exchange;

import org.flexpay.payments.action.outerrequest.request.response.data.ConsumerAttributes;

import java.util.List;

/**
 *
 */
public class SetTotalSquareCalculationPropertyOperation extends SetSquareCalculationPropertyOperation {

	public SetTotalSquareCalculationPropertyOperation(ServiceOperationsFactory factory, List<String> datum) throws InvalidContainerException {
		super(factory, datum);
	}

	@Override
	protected String getConsumerAttributeCode() {
		return ConsumerAttributes.ATTR_TOTAL_SQUARE;
	}
}
