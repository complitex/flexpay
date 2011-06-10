package org.flexpay.common.process;

/**
 * ProcessInstance or its task commands that ProcessManager should be aware of while executing process
 */
public enum LyfecycleVote {

	/**
	 * Task should be started
	 */
	START,
	/**
	 * Task should be postponed
	 */
	POSTPONE,
	/**
	 * Task should be cancelled
	 */
	CANCEL

}
