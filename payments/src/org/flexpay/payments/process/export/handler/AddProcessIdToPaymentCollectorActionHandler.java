package org.flexpay.payments.process.export.handler;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_COLLECTOR_ID;

@Transactional(readOnly = true)
public class AddProcessIdToPaymentCollectorActionHandler extends FlexPayActionHandler {

	private PaymentCollectorService paymentCollectorService;

	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {

		Long paymentCollectorId = (Long)parameters.get(PAYMENT_COLLECTOR_ID);

		PaymentCollector paymentCollector = paymentCollectorService.read(new Stub<PaymentCollector>(paymentCollectorId));

		if (paymentCollector == null) {
			log.error("Payment collector '{}' did not find ", paymentCollectorId);
			return RESULT_ERROR;
		}

		paymentCollector.setTradingDayProcessInstanceId(getProcessId());

		return updatePaymentCollector(paymentCollector)? RESULT_NEXT: RESULT_ERROR;
	}

	@Transactional(readOnly = false)
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
