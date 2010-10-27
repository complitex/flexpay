package org.flexpay.payments.process.export.handler;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.node.DecisionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendSignalToParentProcessHandler implements DecisionHandler {

	private static final Logger log = LoggerFactory.getLogger(SendSignalToParentProcessHandler.class);

	@Override
	public String decide(ExecutionContext executionContext) throws Exception {
		Integer closedCountCashBoxes = (Integer)executionContext.getContextInstance().getVariable("closedCountCashBoxes");

		if (closedCountCashBoxes > 1) {
			log.debug("wait");
			return "wait";
		} else if (closedCountCashBoxes == 1) {
			log.debug("close");
			return "close";
		}

		log.debug("approve");
		return "approve";
	}
}
