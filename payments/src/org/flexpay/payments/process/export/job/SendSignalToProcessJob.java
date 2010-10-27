package org.flexpay.payments.process.export.job;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.TaskHelper;
import org.flexpay.common.process.job.Job;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public abstract class SendSignalToProcessJob extends Job {

	private static final String RESULT_REPEAT = "repeat";

	private ProcessManager processManager;
	private String message;
	private String actorName;

	@Override
	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
		Long processId = getProcessId(parameters);

		if (processId == null) {
			return RESULT_NEXT;
		}

		Set<?> signaledTransitions = TaskHelper.getTransitions(processManager, actorName, processId, message, log, true);
		if (signaledTransitions.isEmpty()) {
			log.warn("Repeat signal from {} to {}", getProcessId(), processId);
			return RESULT_REPEAT;
		}
		return RESULT_NEXT;
	}

	public abstract Long getProcessId(Map<Serializable, Serializable> parameters);

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
