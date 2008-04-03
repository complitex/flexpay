package org.flexpay.ab.sync;

import org.flexpay.ab.service.HistoryDumpService;
import org.flexpay.common.test.SpringBeanAwareTestCase;

public class TestDumpHistoryJob extends SpringBeanAwareTestCase {

	@Override
	protected void runTest() throws Throwable {
		testDumpHistory();
	}

	public void testDumpHistory() throws Throwable {

//		HistoryDumpService dumpService = (HistoryDumpService) applicationContext.getBean("historyDumpService");
//		dumpService.dumpHistory();

		Thread.sleep( 60 * 1000 );
	}
}
