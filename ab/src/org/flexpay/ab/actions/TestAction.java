package org.flexpay.ab.actions;

import org.apache.log4j.Logger;

public class TestAction {

	private static Logger log = Logger.getLogger(TestAction.class);

	public String execute() throws Exception {
		return "success";
	}

	static {
		log.info("Test action loaded");
	}

	{
		log.info("Test action created!");
	}
}
