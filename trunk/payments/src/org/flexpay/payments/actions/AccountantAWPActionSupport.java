package org.flexpay.payments.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.springframework.beans.factory.annotation.Required;

public abstract class AccountantAWPActionSupport extends FPActionSupport {

	// required services
	private PaymentCollectorService paymentCollectorService;

	public String getPaymentCollectorInfoString() {

		PaymentCollector paymentCollector = getPaymentCollector();
		if (paymentCollector == null) {
			return null;
		}
		
		return paymentCollector.getName(getUserPreferences().getLocale());
	}

	private PaymentCollector getPaymentCollector() {

		Long paymentCollectorId = ((PaymentsUserPreferences) getUserPreferences()).getPaymentCollectorId();
		if (paymentCollectorId == null) {
			return null;
		}

		return paymentCollectorService.read(new Stub<PaymentCollector>(paymentCollectorId));
	}

	@Required
	public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
		this.paymentCollectorService = paymentCollectorService;
	}
}
