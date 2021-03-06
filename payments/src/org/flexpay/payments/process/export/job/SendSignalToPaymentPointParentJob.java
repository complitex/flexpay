package org.flexpay.payments.process.export.job;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentPointService;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.CURRENT_INDEX_PAYMENT_POINT;
import static org.flexpay.payments.process.export.ExportJobParameterNames.PAYMENT_POINTS;

public class SendSignalToPaymentPointParentJob extends SendSignalToProcessJob {

	private PaymentPointService paymentPointService;

	@Override
	public Long getProcessId(Map<Serializable, Serializable> parameters) {
		Integer index = (Integer)parameters.get(CURRENT_INDEX_PAYMENT_POINT);
		Long[] paymentPoints = (Long[])parameters.get(PAYMENT_POINTS);

		PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(paymentPoints[index]));

		if (paymentPoint == null) {
			log.error("Payment point {} did not find", paymentPoints[index]);
			return null;
		}

		return paymentPoint.getCollector().getTradingDayProcessInstanceId();
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}
}
