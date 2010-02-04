package org.flexpay.payments.actions.monitor;

import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentPointsListMonitorPageAction extends AccountantAWPActionSupport {

	private static final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

    private String updated;

	private PaymentCollectorService paymentCollectorService;

    @NotNull
    @Override
    protected String doExecute() throws Exception {

		if (!(getUserPreferences() instanceof PaymentsUserPreferences)) {
			log.error("{} is not instanceof {}", getUserPreferences().getClass(), PaymentsUserPreferences.class);
			return ERROR;
		}

		Long paymentCollectorId = ((PaymentsUserPreferences) getUserPreferences()).getPaymentCollectorId();
		if (paymentCollectorId == null) {
			log.error("PaymentCollectorId is not defined in preferences of User {} (id = {})", getUserPreferences().getUsername(), getUserPreferences().getId());
			return ERROR;
		}
		PaymentCollector paymentCollector = paymentCollectorService.read(new Stub<PaymentCollector>(paymentCollectorId));
		if (paymentCollector == null) {
			log.error("No payment collector found with id {}", paymentCollectorId);
			return ERROR;
		}

        updated = formatTime.format(new Date());

        return SUCCESS;
    }

    @NotNull
    @Override
    protected String getErrorResult() {
        return ERROR;
    }

    public String getUpdated() {
        return updated;
    }

	@Required
	public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
		this.paymentCollectorService = paymentCollectorService;
	}

}
