package org.flexpay.payments.process.export.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoModeDecisionHandler {

	private static final Logger log = LoggerFactory.getLogger(SendSignalToParentProcessHandler.class);

	public String decide() throws Exception {
		/*
		String autoMode = (String)executionContext.getContextInstance().getVariable("AUTO_MODE");

		if (StringUtils.equals(autoMode, "true")) {
			log.debug("auto");
			return "auto";
		}

		log.debug("manual");
		return "manual";
		*/
		return "";
	}
}
