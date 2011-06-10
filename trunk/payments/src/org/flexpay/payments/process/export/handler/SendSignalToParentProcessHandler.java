package org.flexpay.payments.process.export.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendSignalToParentProcessHandler {

	private static final Logger log = LoggerFactory.getLogger(SendSignalToParentProcessHandler.class);

	public String decide() throws Exception {
		/*

		Integer closedCountCashboxes = (Integer) executionContext.getContextInstance().getVariable("closedCountCashboxes");

		if (closedCountCashboxes > 1) {
			log.debug("wait");
			return "wait";
		} else if (closedCountCashboxes == 1) {
			log.debug("close");
			return "close";
		}

		log.debug("cancel");
		return "cancel";
		*/
		return "";
	}
}
