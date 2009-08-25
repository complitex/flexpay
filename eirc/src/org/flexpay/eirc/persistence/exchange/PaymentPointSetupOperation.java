package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PaymentPointSetupOperation extends ContainerOperation {

	private ServiceOperationsFactory factory;
	private Long paymentPointId;

	public PaymentPointSetupOperation(ServiceOperationsFactory factory, List<String> datum)
			throws InvalidContainerException {

		super(Integer.valueOf(datum.get(0)));
		this.factory = factory;

		try {
			paymentPointId = Long.parseLong(datum.get(1));
		} catch (NumberFormatException ex) {
			throw new InvalidContainerException("Cannot parse payment point id: " + datum);
		}
	}

	/**
	 * Process operation
	 *
	 * @param context ProcessingContext
	 * @return DelayedUpdate object
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	@Override
	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException {

		final PaymentPoint point = factory.getPaymentPointService().read(new Stub<PaymentPoint>(paymentPointId));
		if (point == null) {
			throw new FlexPayException("Cannot find payment point by id: " + paymentPointId);
		}

		context.visitCurrentRecordUpdates(new DelayedUpdateVisitor() {
			@Override
			public void apply(DelayedUpdate update) {
				if (update instanceof PaymentPointAwareUpdate) {
					((PaymentPointAwareUpdate) update).setPoint(point);
				}
			}
		});

		return DelayedUpdateNope.INSTANCE;
	}
}
