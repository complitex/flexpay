package org.flexpay.payments.process.export.handler;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.TaskHelper;
import org.flexpay.common.process.handler.FlexPayActionHandler;
import org.jbpm.JbpmException;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ProcessInstance;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;
import java.util.Set;


public abstract class SendSignalToProcessActionHandler extends FlexPayActionHandler {

	private static final String RESULT_REPEAT = "repeat";

	private ProcessManager processManager;
	private String message;
	private String actorName;

	@Override
	final public String execute2(Map<String, Object> parameters) throws FlexPayException {

		Long processId = getProcessId(parameters);

		if (processId == null) {
			return RESULT_NEXT;
		}

		Set<?> signaledTransitions = TaskHelper.getTransitions(processManager, actorName, processId, message, log, false);
		if (signaledTransitions.isEmpty()) {
			log.warn("Repeat signal from {} to {}", getProcessId(), processId);
			return RESULT_REPEAT;
		}
		return RESULT_NEXT;
	}

	public abstract Long getProcessId(Map<String, Object> parameters);

	@Required
	public void setMessage(String message) {
		this.message = message;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}

	@Required
	public void setActorName(String actorName) {
		this.actorName = actorName;
	}
}
