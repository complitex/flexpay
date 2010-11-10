package org.flexpay.payments.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.actions.interceptor.CashboxAware;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.springframework.beans.factory.annotation.Required;

public abstract class OperatorAWPActionSupport extends FPActionSupport implements CashboxAware {

	protected CashboxService cashboxService;
	protected PaymentPointService paymentPointService;

	public String getCashboxInfoString() {

		Cashbox cashbox = getCashbox();
		if (cashbox == null) {
			return null;
		}

		return cashbox.getName(getUserPreferences().getLocale());
	}

	public String getPaymentPointInfoString() {

		PaymentPoint paymentPoint = getPaymentPoint();
		if (paymentPoint == null) {
			return null;
		}

		return paymentPoint.getName(getUserPreferences().getLocale());
	}

	private Cashbox getCashbox() {
		Long cashboxId = getCashboxId();

		if (cashboxId == null) {
			return null;
		}

		return cashboxService.read(new Stub<Cashbox>(cashboxId));
	}

	protected PaymentPoint getPaymentPoint() {

		Cashbox cashbox = getCashbox();
		if (cashbox == null) {
			return null;
		}

		return paymentPointService.read(cashbox.getPaymentPointStub());
	}

    protected Long getPaymentProcessId() {
        return getPaymentPoint().getTradingDayProcessInstanceId();
    }

	@Override
	public Long getCashboxId() {
		
		UserPreferences userPreferences = getUserPreferences();
		if (userPreferences instanceof PaymentsUserPreferences) {
			return ((PaymentsUserPreferences)userPreferences).getPaymentCollectorId();
		}
		return null;
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}
}
