package org.flexpay.common.process.handler2;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.process.exception.ProcessDefinitionException;
import org.flexpay.common.process.exception.ProcessInstanceException;
import org.flexpay.common.process.handler.TaskHandler;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

import static org.flexpay.common.process.ProcessManager.PARAM_SECURITY_CONTEXT;

public class DemonProcessInstanceHandler extends TaskHandler {

	private ProcessManager processManager;

	public static final String PROCESS_DEFINITION_ID = "processDefinitionId";

	@Override
	public String execute(final Map<String, Object> parameters) throws FlexPayException {

		if (!parameters.containsKey(PROCESS_DEFINITION_ID)) {
			log.error("Can not find parameter {} (process {})", PROCESS_DEFINITION_ID, getProcessInstanceId(parameters));
			return RESULT_ERROR;
		}

        Authentication runAuthentication;
		if (parameters.containsKey(PARAM_SECURITY_CONTEXT)
						&& parameters.get(PARAM_SECURITY_CONTEXT) instanceof Authentication) {
			runAuthentication = (Authentication)parameters.get(PARAM_SECURITY_CONTEXT);
			log.debug("Authentication in parameters: {}", runAuthentication);
		} else {
			runAuthentication = SecurityUtil.getAuthentication();
			parameters.put(PARAM_SECURITY_CONTEXT, runAuthentication);
		}

        final Authentication auth = runAuthentication;

        final long parentId = Thread.currentThread().getId();
		final String processDefinitionId = (String)parameters.get(PROCESS_DEFINITION_ID);
        new Thread(new Runnable() {
			@Override
			public void run() {

                while(!isCompleted()) {
                    log.debug("Wait while completed {} {}", DemonProcessInstanceHandler.class, parentId);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        log.warn("Iterrupted: {}", e);
                        break;
                    }
                }
                SecurityContextHolder.getContext().setAuthentication(auth);
                ProcessInstance processInstance = null;
                try {
                    processInstance = processManager.startProcess(processDefinitionId, parameters);
                } catch (Exception e) {
                    log.error("Exception {}", e);
                }

                if (processInstance == null) {
                    log.error("Can not start process instance by process definition id {}", processDefinitionId);
                }
                log.debug("Started process instance: {}", processInstance);
            }
        }).start();

		return RESULT_NEXT;
	}

	@Required
	public void setProcessManager(ProcessManager processManager) {
		this.processManager = processManager;
	}
}
