package org.flexpay.payments.service.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.payments.service.GeneralizationTradingDay;
import org.jetbrains.annotations.NotNull;

public class CashboxTradingDay extends GeneralizationTradingDay<Cashbox> {

	private static final String AUTOMATION = "AUTOMATION";

	@Override
	public boolean startTradingDay(@NotNull Cashbox cashbox) throws FlexPayException {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void stopTradingDay(@NotNull Cashbox cashbox) throws FlexPayException {
		ProcessInstance processInstance = processManager.getProcessInstance(cashbox.getTradingDayProcessInstanceId());
		if (processInstance == null) {
			throw new FlexPayException("Process instance did not find by id " + cashbox.getTradingDayProcessInstanceId() +
					" for cashbox id " + cashbox.getId());
		}
		if (processInstance.hasEnded()) {
			throw new FlexPayException("Process instance " + processInstance.getId() + " ended for cash box id " + cashbox.getId());
		}
		processManager.messageExecution(processInstance, "closeTradingDayByAdmin", "CloseByAdmin");
		//TaskHelper.getTransitions(processManager, AUTOMATION, cashbox.getTradingDayProcessInstanceId(), ".auto", log);
	}
}
