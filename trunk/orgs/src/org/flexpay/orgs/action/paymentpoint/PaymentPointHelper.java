package org.flexpay.orgs.action.paymentpoint;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentPointService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Locale;

public class PaymentPointHelper {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private PaymentPointService paymentPointService;

	public String getDescription(@NotNull PaymentPoint point, @NotNull Locale locale) {
		return point.getAddress();
	}

	public String getDescription(@NotNull Stub<PaymentPoint> pointStub, @NotNull Locale locale) {

		PaymentPoint point = paymentPointService.read(pointStub);
		if (point == null) {
			log.info("No payment point for stub {}", pointStub);
			return null;
		}

		return getDescription(point, locale);
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

}
