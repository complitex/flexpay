package org.flexpay.payments.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.springframework.beans.factory.annotation.Required;

public abstract class AccountantAWPActionSupport extends FPActionSupport {

	protected PaymentCollectorService paymentCollectorService;

	public String getPaymentCollectorInfoString() {

		PaymentCollector paymentCollector = getPaymentCollector();
		if (paymentCollector == null) {
			return null;
		}
		
		return paymentCollector.getName(getUserPreferences().getLocale());
	}

    public PaymentCollector getPaymentCollector() {

        Long paymentCollectorId = ((PaymentsUserPreferences) getUserPreferences()).getPaymentCollectorId();
        if (paymentCollectorId == null) {
            log.warn("PaymentCollectorId is not defined in preferences of User {} (id = {})", getUserPreferences().getUsername(), getUserPreferences().getId());
            return null;
        }

        PaymentCollector paymentCollector = paymentCollectorService.read(new Stub<PaymentCollector>(paymentCollectorId));
        if (paymentCollector == null) {
            log.error("No payment collector found with id {}", paymentCollectorId);
            return null;
        } else if (paymentCollector.isNotActive()) {
            log.warn("Payment collector with id {} is not active", paymentCollectorId);
        }

        return paymentCollector;
    }

	@Required
	public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
		this.paymentCollectorService = paymentCollectorService;
	}
}
