package org.flexpay.eirc.persistence.exchange;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.exchange.delayed.DelayedUpdateNope;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.ServiceProvider;
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
	 * ProcessInstance operation
	 *
	 * @param context ProcessingContext
	 * @return DelayedUpdate object
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if failure occurs
	 */
	@Override
	public DelayedUpdate process(@NotNull ProcessingContext context) throws FlexPayException {

		final PaymentPoint point = getPaymentPoint(context);
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

    private PaymentPoint getPaymentPoint(@NotNull ProcessingContext context) {
        ServiceProvider serviceProvider = factory.getServiceProviderService().getProvider(new Stub<Organization>(context.getRegistry().getRecipientCode()));
        if (serviceProvider != null) {
            return factory.getPaymentPointService().read(new Stub<PaymentPoint>(paymentPointId));
        }
        Organization senderOrganization = factory.getOrganizationService().readFull(new Stub<Organization>(context.getRegistry().getSenderCode()));
        if (senderOrganization != null) {
            DataSourceDescription dsDescription = senderOrganization.getDataSourceDescription();
            Stub<PaymentPoint> paymentPointStub = factory.getCorrectionsService().findCorrection(String.valueOf(paymentPointId), PaymentPoint.class, Stub.stub(dsDescription));
            if (paymentPointStub != null) {
                return factory.getPaymentPointService().read(paymentPointStub);
            }
        }
        return null;
    }
}
