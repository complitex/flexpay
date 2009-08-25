package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimplePaymentOperation extends ContainerOperation {

	private ServiceOperationsFactory factory;

	private Long organizationId;

	public SimplePaymentOperation(ServiceOperationsFactory factory, List<String> datum) throws InvalidContainerException {
		super(Integer.valueOf(datum.get(0)));
		if (datum.size() < 2) {
			throw new InvalidContainerException("Invalid change personal account operation data");
		}

		try {
			organizationId = Long.valueOf(datum.get(1));
		} catch (NumberFormatException e) {
			throw new InvalidContainerException("Cannot parse organization id: " + datum.get(1), e);
		}
		this.factory = factory;
	}

	/**
	 * Process operation
	 *
	 * @param context ProcessingContext 
	 * @return DelayedUpdate object
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if failure occurs
	 */
	@Override
	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException, FlexPayExceptionContainer {

		throw new IllegalStateException("Not implemented ");
	}
}
