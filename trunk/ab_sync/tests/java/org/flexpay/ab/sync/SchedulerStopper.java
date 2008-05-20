package org.flexpay.ab.sync;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.quartz.Scheduler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Disable scheduler startup
 */
public class SchedulerStopper extends SpringBeanAwareTestCase {

	private Scheduler scheduler;

	@Test
	public void testSchedulerDisabled() throws Throwable {
		assertTrue("Scheduler was not disabled", scheduler == null || scheduler.isShutdown());
	}

	@Before
	public void prepareTestInstance() throws Exception {
		scheduler = (Scheduler) applicationContext.getBean("schedulerFactoryBeanAbSync");
		if (scheduler != null) {
			scheduler.shutdown();
		}
	}
}
