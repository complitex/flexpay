package org.flexpay.common.process.handler2;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.handler.TaskHandler;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Map;

public class SendSignal2ProcessInstanceHandler extends TaskHandler {

	private ProcessManager processManager;

	private static final String WAITING_PROCESS_INSTANCE_ID = "waitingProcessInstanceId";
	private static final String SIGNAL_VALUE = "signalValue";
	private static final String TRANSITIONS = "transitions";
	private static final Long TIME_OUT = 2000L;

	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

		Long processId = required(WAITING_PROCESS_INSTANCE_ID, parameters);
		String signal = required(SIGNAL_VALUE, parameters);

		int i = 10;
		ProcessInstance processInstance;
		while (i > 0) {
			processInstance = processManager.getProcessInstance(processId);
			if (processInstance == null) {
				log.error("Can not find process instance by process id '{}'", processId);
				return RESULT_ERROR;
			} if (processInstance.getParameters().containsKey(TRANSITIONS) &&
					processInstance.getParameters().get(TRANSITIONS) instanceof List && ((List)processInstance.getParameters().get(TRANSITIONS)).contains(signal)) {
				log.debug("Send signal '{}' to {} from {}", new Object[]{signal, processId, getProcessInstanceId(parameters)});
				processManager.signalExecution(processInstance, signal);
				return RESULT_NEXT;
			}
			log.warn("Can not find '{}' or signal value '{}' on reference process instance {}. Time out is {} msec.",
					new Object[]{TRANSITIONS, signal, processId, TIME_OUT});
			try {
				Thread.sleep(TIME_OUT);
			} catch (InterruptedException e) {
				log.debug("Interrupted send signal", e);
			}
			i--;
		}

		log.error("Can not send signal value '{}' to process instance {}", signal, processId);

		return RESULT_ERROR;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
