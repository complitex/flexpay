package org.flexpay.common.process.handler;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.process.ProcessManager;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

import java.util.Map;

public abstract class FlexPayActionHandler implements ActionHandler {

    protected final Logger processLog = ProcessLogger.getLogger(getClass());
	protected final Logger log = LoggerFactory.getLogger(getClass());

	public final static String RESULT_NEXT = "next";
	public final static String RESULT_ERROR = "error";

	private Long processId;

	@SuppressWarnings ({"unchecked"})
	@Override
	public void execute(ExecutionContext executionContext) throws Exception {
		log.debug("Full name current token: {}", executionContext.getToken().getFullName());
		if (executionContext.getToken().getNode().getParent() != null) {
			log.debug("Parent node of current token: {}", executionContext.getToken().getNode().getParent().getName());
		}
		ContextInstance contextInstance = executionContext.getContextInstance();
		Map<String, Object> parameters = (Map<String, Object>) contextInstance.getVariables();

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication((Authentication) parameters.get(ProcessManager.PARAM_SECURITY_CONTEXT));
        }

		processId = contextInstance.getProcessInstance().getId(); //(String)parameters.get("ProcessInstanceID");

		String result = execute2(parameters);

		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			contextInstance.setVariable(entry.getKey(), entry.getValue());
			//log.debug("{}={}", new Object[]{entry.getKey(), entry.getValue()});
		}

		contextInstance.setVariable(FlexPayDecisionHandler.RESULT, result);
		//log.debug("{}={}", new Object[]{FlexPayDecisionHandler.RESULT, result});
	}

	public Long getProcessId() {
		return processId;
	}

	public abstract String execute2(Map<String, Object> parameters) throws FlexPayException;
}
