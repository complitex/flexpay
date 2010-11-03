package org.flexpay.payments.service;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.process.ContextCallback;
import org.flexpay.common.process.ProcessManager;
import org.jbpm.JbpmContext;
import org.jbpm.graph.exe.ProcessInstance;
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
		return processManager.execute(new ContextCallback<Boolean>() {
            @Override
			public Boolean doInContext(@NotNull JbpmContext context) {
				ProcessInstance processInstance = context.getProcessInstance(processInstanceId);
				if (processInstance == null || processInstance.hasEnded()){
					log.debug("Process Instance {} was not found", processInstanceId);
					return false;
				}
				String canCreateOrUpdate = (String) context.getProcessInstance(processInstanceId)
						.getContextInstance().getVariable(CAN_UPDATE_OR_CREATE_OPERATION);
				log.debug("CAN_UPDATE_OR_CREATE_OPERATION = {} for process instance id = {}", canCreateOrUpdate, processInstanceId);
				return Boolean.valueOf(canCreateOrUpdate);
			}
		});
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}