package org.flexpay.ab.sync;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.ab.service.SyncService;
import org.flexpay.ab.service.HistoryDumpService;

public class TestDumpHistoryJob extends SpringBeanAwareTestCase {

	@Override
	protected void runTest() throws Throwable {
		testDumpHistory();
	}

	public void testDumpHistory() throws Throwable {

		HistoryDumpService dumpService = (HistoryDumpService) applicationContext.getBean("historyDumpService");
		dumpService.dumpHistory();
	}
}
