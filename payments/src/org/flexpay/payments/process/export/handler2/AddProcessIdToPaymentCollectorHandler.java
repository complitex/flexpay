package org.flexpay.payments.process.export.handler2;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.handler.TaskHandler;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_COLLECTOR_ID;

public class AddProcessIdToPaymentCollectorHandler extends TaskHandler {
	private PaymentCollectorService paymentCollectorService;

	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

		Long paymentCollectorId = required(PAYMENT_COLLECTOR_ID, parameters);

		PaymentCollector paymentCollector = paymentCollectorService.read(new Stub<PaymentCollector>(paymentCollectorId));

		if (paymentCollector == null) {
			log.error("Payment collector '{}' did not find ", paymentCollectorId);
			return RESULT_ERROR;
		}

		paymentCollector.setTradingDayProcessInstanceId(getProcessInstanceId(parameters));

		return updatePaymentCollector(paymentCollector)? RESULT_NEXT: RESULT_ERROR;
	}

	private boolean updatePaymentCollector(PaymentCollector paymentCollector) {
		try {
			paymentCollectorService.update(paymentCollector);
			return true;
		} catch (FlexPayExceptionContainer flexPayExceptionContainer) {
			log.error("Failed update payment collector", flexPayExceptionContainer);
		}
		return false;
	}

	@Required
	public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
		this.paymentCollectorService = paymentCollectorService;
	}
}
