package org.flexpay.eirc.persistence.exchange;

import org.flexpay.payments.action.outerrequest.request.response.data.ConsumerAttributes;

import java.util.List;

/**
 *
 */
public class SetLiveSquareCalculationPropertyOperation extends SetSquareCalculationPropertyOperation {

	public SetLiveSquareCalculationPropertyOperation(ServiceOperationsFactory factory, List<String> datum) throws InvalidContainerException {
		super(factory, datum);
	}

	@Override
	protected String getConsumerAttributeCode() {
		return ConsumerAttributes.ATTR_LIVE_SQUARE;
	}
}
