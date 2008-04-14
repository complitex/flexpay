package org.flexpay.ab.sync;

import org.flexpay.common.test.SpringBeanAwareTestCase;

public class TestDumpHistoryJob extends SpringBeanAwareTestCase {

	@Override
	protected void runTest() throws Throwable {
		testDumpHistory();
	}

	public void testDumpHistory() throws Throwable {

		// let a scheduled task do the job
//		Thread.sleep( 60 * 1000 );
	}
}
