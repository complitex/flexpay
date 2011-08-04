package org.flexpay.payments.service;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import static org.flexpay.payments.util.PaymentCollectorTradingDayConstants.CAN_UPDATE_OR_CREATE_OPERATION;

public abstract class GeneralizationTradingDay<T extends DomainObject> implements TradingDay<T> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	protected ProcessManager processManager;

	@Override
	public boolean isOpened(@NotNull final Long processInstanceId) {
		ProcessInstance processInstance = processManager.getProcessInstance(processInstanceId);
		if (processInstance == null || processInstance.hasEnded()) {
			log.debug("ProcessInstance Instance {} was not found", processInstanceId);
			return false;
		}
		Boolean canCreateOrUpdate = (Boolean) processInstance.getParameter(CAN_UPDATE_OR_CREATE_OPERATION);
		log.debug("{} = {} for process instance id = {}", new Object[]{CAN_UPDATE_OR_CREATE_OPERATION, canCreateOrUpdate, processInstanceId});
		if (canCreateOrUpdate == null) {
			log.debug("{} is null, then return false", CAN_UPDATE_OR_CREATE_OPERATION);
			return false;
		}
		return canCreateOrUpdate;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
