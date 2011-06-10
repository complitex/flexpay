package org.flexpay.payments.service.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.payments.service.GeneralizationTradingDay;
import org.jetbrains.annotations.NotNull;

public class CashboxTradingDay extends GeneralizationTradingDay<Cashbox> {

	private static final String AUTOMATION = "AUTOMATION";

	@Override
	public void startTradingDay(@NotNull Cashbox cashbox) throws FlexPayException {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void stopTradingDay(@NotNull Cashbox cashbox) throws FlexPayException {
		//TaskHelper.getTransitions(processManager, AUTOMATION, cashbox.getTradingDayProcessInstanceId(), ".auto", log);
	}
}
