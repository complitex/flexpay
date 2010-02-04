package org.flexpay.payments.actions;

import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.actions.interceptor.CashboxAware;
import org.springframework.beans.factory.annotation.Required;

public abstract class OperatorAWPWithPagerActionSupport<T> extends FPActionWithPagerSupport<T> implements CashboxAware {

	protected Long cashboxId;

	// required services
	private CashboxService cashboxService;
	private PaymentPointService paymentPointService;

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

		if (cashboxId == null) {
			return null;
		}

		return cashboxService.read(new Stub<Cashbox>(cashboxId));
	}

	private PaymentPoint getPaymentPoint() {

		Cashbox cashbox = getCashbox();
		if (cashbox == null) {
			return null;
		}

		return paymentPointService.read(cashbox.getPaymentPointStub());
	}

	@Override
	public Long getCashboxId() {
		return cashboxId;
	}

	@Override
	public void setCashboxId(Long cashboxId) {
		this.cashboxId = cashboxId;
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
