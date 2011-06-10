package org.flexpay.payments.process.export.handler2;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.handler.TaskHandler;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_COLLECTOR_ID;
import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_POINTS;

public class GetPaymentPointsHandler extends TaskHandler {

	private PaymentCollectorService paymentCollectorService;

	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

		Long paymentCollectorId = required(PAYMENT_COLLECTOR_ID, parameters);

		List<Long> paymentPointsCollection = list();

		completePaymentPointCollection(paymentCollectorId, paymentPointsCollection);

		parameters.put(PAYMENT_POINTS, paymentPointsCollection);

		return RESULT_NEXT;
	}

	private void completePaymentPointCollection(Long paymentCollectorId, List<Long> paymentPointsCollection) {
		/*
		for (Long paymentPointId = 0L; paymentPointId < 50L; paymentPointId++) {
			paymentPointsCollection.add(paymentPointId);
		}
		*/
		PaymentCollector paymentCollector = paymentCollectorService.read(new Stub<PaymentCollector>(paymentCollectorId));
		if (paymentCollector == null) {
			log.warn("Payment collector '{}' did not find ", paymentCollectorId);
		} else {
			for (PaymentPoint paymentPoint : paymentCollector.getPaymentPoints()) {
				paymentPointsCollection.add(paymentPoint.getId());
			}
		}
	}

	@Required
	public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
		this.paymentCollectorService = paymentCollectorService;
	}
}
