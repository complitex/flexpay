package org.flexpay.payments.service.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.payments.service.GeneralizationTradingDay;
import org.flexpay.payments.service.TradingDay;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class PaymentPointTradingDay extends GeneralizationTradingDay<PaymentPoint> {

	private CashboxService cashboxService;
	private TradingDay<Cashbox> cashboxTradingDayService;

	@Override
	public void startTradingDay(@NotNull PaymentPoint paymentPoint) throws FlexPayException {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void stopTradingDay(@NotNull PaymentPoint paymentPoint) throws FlexPayException {
		for (Cashbox cashbox : cashboxService.findCashboxesForPaymentPoint(paymentPoint.getId())) {
			cashboxTradingDayService.stopTradingDay(cashbox);
		}
	}

	@Required
	public void setCashboxService(CashboxService cashboxService) {
		this.cashboxService = cashboxService;
	}

	@Required
	public void setCashboxTradingDayService(TradingDay<Cashbox> cashboxTradingDayService) {
		this.cashboxTradingDayService = cashboxTradingDayService;
	}
}
