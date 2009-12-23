package org.flexpay.common.process.handler;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.DecisionHandler;

public class FlexPayDecisionHandler implements DecisionHandler {
	public static final String RESULT = "result";
	@Override
	public String decide(ExecutionContext executionContext) throws Exception {
		return (String)executionContext.getContextInstance().getVariable(RESULT);
	}
}
