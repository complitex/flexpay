package org.flexpay.common.process.handler2;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.handler.TaskHandler;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class DemonProcessInstanceHandler extends TaskHandler {

	private ProcessManager processManager;

	public static final String PROCESS_DEFINITION_ID = "processDefinitionId";

	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

		if (!parameters.containsKey(PROCESS_DEFINITION_ID)) {
			log.error("Can not find parameter {} (process {})", PROCESS_DEFINITION_ID, getProcessInstanceId(parameters));
			return RESULT_ERROR;
		}

		String processDefinitionId = (String)parameters.get(PROCESS_DEFINITION_ID);
		ProcessInstance processInstance = processManager.startProcess(processDefinitionId, parameters);

		if (processInstance == null) {
			log.error("Can not start process instance by process definition id {}", processDefinitionId);
			return RESULT_ERROR;
		}
		log.debug("Started process instance: {}", processInstance);

		return RESULT_NEXT;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
