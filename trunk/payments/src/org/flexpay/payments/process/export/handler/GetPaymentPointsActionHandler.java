package org.flexpay.payments.process.export.handler;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_COLLECTOR_ID;
import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_POINTS;

public class GetPaymentPointsActionHandler extends FlexPayActionHandler {

	private PaymentCollectorService paymentCollectorService;

	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {

		Long paymentCollectorId = (Long)parameters.get(PAYMENT_COLLECTOR_ID);

		PaymentCollector paymentCollector = paymentCollectorService.read(new Stub<PaymentCollector>(paymentCollectorId));
		List<Long> paymentPointsCollection = list();

		if (paymentCollector == null) {
			log.error("Payment collector '{}' did not find ", paymentCollectorId);
		} else {
			for (PaymentPoint paymentPoint : paymentCollector.getPaymentPoints()) {
				paymentPointsCollection.add(paymentPoint.getId());
			}
		}

		parameters.put(PAYMENT_POINTS, paymentPointsCollection.toArray(new Long[paymentPointsCollection.size()]));

		return RESULT_NEXT;
	}

	@Required
	public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
		this.paymentCollectorService = paymentCollectorService;
	}
}
