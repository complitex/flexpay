package org.flexpay.payments.actions.monitor;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.payments.actions.AccountantAWPActionSupport;
import org.flexpay.payments.service.Roles;
import org.flexpay.payments.service.TradingDay;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.annotation.Secured;

public class PaymentCollectorEnableDisableAction extends AccountantAWPActionSupport {

	public static final String ENABLE = "enable";
	public static final String DISABLE = "disable";

	private String action;

	private TradingDay<PaymentCollector> tradingDayService;

    @NotNull
    @Override
    protected String doExecute() throws Exception {

        if (!(getUserPreferences() instanceof PaymentsUserPreferences)) {
            log.error("{} is not instanceof {}", getUserPreferences().getClass(), PaymentsUserPreferences.class);
            return SUCCESS;
        }

        PaymentsUserPreferences userPreferences = (PaymentsUserPreferences) getUserPreferences();

        Long paymentCollectorId = userPreferences.getPaymentCollectorId();
        if (paymentCollectorId == null) {
            log.error("PaymentCollectorId is not defined in preferences of User {} (id = {})", userPreferences.getUsername(), userPreferences.getId());
            return SUCCESS;
        }

		PaymentCollector paymentCollector = paymentCollectorService.read(new Stub<PaymentCollector>(paymentCollectorId));

        if (paymentCollector == null || paymentCollector.isNew()) {
            log.warn("Incorrect payment point id {}", paymentCollector);
            return SUCCESS;
        }

        if (DISABLE.equals(action) && paymentCollector.getTradingDayProcessInstanceId() != null) {
            disableTradingDay(paymentCollector);
            addActionMessage(getText("payments.payment_points.list.disable_trading_day", "Disable trading day for {0} payment point", paymentCollector.getName(getLocale())));
        } else if (ENABLE.equals(action) && paymentCollector.getTradingDayProcessInstanceId() == null) {
            enableTradingDay(paymentCollector);
            addActionMessage(getText("payments.payment_points.list.enable_trading_day", "Enable trading day for {0} payment point", paymentCollector.getName(getLocale())));
        }

        return SUCCESS;
    }

	@Secured (Roles.TRADING_DAY_ADMIN_ACTION)
	private void enableTradingDay(PaymentCollector paymentCollector) throws Exception {

		log.debug("Trying to enable trading day for payment collector {}", paymentCollector.getId());

		tradingDayService.startTradingDay(paymentCollector);
	}

	@SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Secured (Roles.TRADING_DAY_ADMIN_ACTION)
	private void disableTradingDay(PaymentCollector paymentCollector) throws FlexPayException {

		log.debug("Trying to disable trading day for payment collector {}", paymentCollector.getId());

		tradingDayService.stopTradingDay(paymentCollector);
	}

    @NotNull
    @Override
    protected String getErrorResult() {
		return SUCCESS;
    }

	public void setAction(String action) {
		this.action = action;
	}

	@Required
	public void setTradingDayService(TradingDay<PaymentCollector> tradingDayService) {
		this.tradingDayService = tradingDayService;
	}
}
