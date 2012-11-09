package org.flexpay.payments.process.export.handler2;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.handler.ProcessInstanceExecuteHandler;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentPointService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_POINT_ID;

public class AddProcessIdToPaymentPointHandler extends ProcessInstanceExecuteHandler {

	private PaymentPointService paymentPointService;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

		Long paymentPointId = required(PAYMENT_POINT_ID, parameters);

		log.debug("Payment point: {}, process instance id: {}",
				new Object[]{paymentPointId, getProcessInstanceId(parameters)});

		PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(paymentPointId));

		if (paymentPoint == null) {
			log.error("Payment point {} did not find", paymentPointId);
			return RESULT_ERROR;
		}

		paymentPoint.setTradingDayProcessInstanceId(getProcessInstanceId(parameters));
		updatePaymentPoint(paymentPoint);

		return RESULT_NEXT;
	}

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
