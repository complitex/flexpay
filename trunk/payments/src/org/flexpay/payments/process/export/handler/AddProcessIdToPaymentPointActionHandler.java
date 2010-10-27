package org.flexpay.payments.process.export.handler;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentPointService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.CURRENT_INDEX_PAYMENT_POINT;
import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_POINTS;

@Transactional(readOnly = true)
public class AddProcessIdToPaymentPointActionHandler extends FlexPayActionHandler {

	private PaymentPointService paymentPointService;

	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {

		Integer index = (Integer)parameters.get(CURRENT_INDEX_PAYMENT_POINT);
		Long[] paymentPoints = (Long[])parameters.get(PAYMENT_POINTS);

		if (paymentPoints == null) {
			log.debug("Parameter '{}' did not find in process context", PAYMENT_POINTS);
			return RESULT_ERROR;
		}
		if (index == null) {
			log.debug("Parameter '{}' did not find in process context", CURRENT_INDEX_PAYMENT_POINT);
			return RESULT_ERROR;
		}

		Long paymentPointId = paymentPoints[index];

		log.debug("Payment point: {}, process instance id: {}",
				new Object[]{paymentPointId, getProcessId()});

		PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(paymentPointId));

		if (paymentPoint == null) {
			log.error("Payment point {} did not find", paymentPointId);
			return RESULT_ERROR;
		}

		paymentPoint.setTradingDayProcessInstanceId(getProcessId());
		updatePaymentPoint(paymentPoint);

		return RESULT_NEXT;
	}

	@Transactional (readOnly = false)
	private void updatePaymentPoint(PaymentPoint paymentPoint) {
		try {
			paymentPointService.update(paymentPoint);
		} catch (FlexPayExceptionContainer flexPayExceptionContainer) {
			log.error("Failed update payment point", flexPayExceptionContainer);
		}
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}
}
