package org.flexpay.payments.actions.monitor;

import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static org.flexpay.payments.actions.monitor.MonitorUtils.formatTime;

public class PaymentPointsListMonitorPageAction extends AccountantAWPActionSupport {

    private String updated;

    @NotNull
    @Override
    protected String doExecute() throws Exception {

		if (!(getUserPreferences() instanceof PaymentsUserPreferences)) {
			log.error("{} is not instanceof {}", getUserPreferences().getClass(), PaymentsUserPreferences.class);
			return ERROR;
		}

		PaymentCollector paymentCollector = getPaymentCollector();
		if (paymentCollector == null) {
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

}
