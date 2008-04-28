package org.flexpay.ab.sync;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.quartz.Scheduler;

/**
 * Disable scheduler startup
 */
public class SchedulerStopper extends SpringBeanAwareTestCase {
	private Scheduler scheduler;

	/**
	 * Override to run the test and assert its state.
	 *
	 * @throws Throwable if any exception is thrown
	 */
	protected void runTest() throws Throwable {
		assertTrue("Scheduler was not disabled", scheduler == null || scheduler.isShutdown());
	}

	protected void prepareTestInstance() throws Exception {
		scheduler = (Scheduler) applicationContext.getBean("schedulerFactoryBeanAbSync");
		if (scheduler != null) {
			scheduler.shutdown();
		}
	}
}
