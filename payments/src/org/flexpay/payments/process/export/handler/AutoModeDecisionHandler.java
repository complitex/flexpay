package org.flexpay.payments.process.export.handler;

import org.apache.commons.lang.StringUtils;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.DecisionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoModeDecisionHandler implements DecisionHandler {

	private static final Logger log = LoggerFactory.getLogger(SendSignalToParentProcessHandler.class);

	@Override
	public String decide(ExecutionContext executionContext) throws Exception {
		String autoMode = (String)executionContext.getContextInstance().getVariable("AUTO_MODE");

		if (StringUtils.equals(autoMode, "true")) {
			log.debug("auto");
			return "auto";
		}

		log.debug("manual");
		return "manual";
	}
}
