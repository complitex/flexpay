package org.flexpay.common.process.handler;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.ProcessLogger;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class FlexPayActionHandler implements ActionHandler {
    protected final Logger processLog = ProcessLogger.getLogger(getClass());
	protected final Logger log = LoggerFactory.getLogger(getClass());

	public final static String RESULT_NEXT = "next";
	public final static String RESULT_ERROR = "error";

	@SuppressWarnings ({"unchecked"})
	@Override
	public void execute(ExecutionContext executionContext) throws Exception {
		Map<String, Object> parameters = (Map<String,Object>)executionContext.getContextInstance().getVariables();
		String result = execute2(parameters);
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			executionContext.getContextInstance().setVariable(entry.getKey(), entry.getValue());
			log.debug("{}={}", new Object[]{entry.getKey(), entry.getValue()});
		}

		executionContext.getContextInstance().setVariable(FlexPayDecisionHandler.RESULT, result);
	}

	public abstract String execute2(Map<String, Object> parameters) throws FlexPayException;
}
