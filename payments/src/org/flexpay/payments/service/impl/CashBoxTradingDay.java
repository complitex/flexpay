package org.flexpay.payments.service.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.TaskHelper;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.payments.process.handlers.AccounterAssignmentHandler;
import org.flexpay.payments.service.GeneralizationTradingDay;
import org.jetbrains.annotations.NotNull;

public class CashBoxTradingDay extends GeneralizationTradingDay<Cashbox> {
	@Override
	public void startTradingDay(@NotNull Cashbox cashBox) throws FlexPayException {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void stopTradingDay(@NotNull Cashbox cashBox) throws FlexPayException {
		TaskHelper.getTransitions(processManager, AccounterAssignmentHandler.ACCOUNTER, cashBox.getTradingDayProcessInstanceId(), ".auto", log);
	}
}
