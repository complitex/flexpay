package org.flexpay.ab.sync;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.ab.service.SyncService;
import org.flexpay.ab.service.HistoryDumpService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDumpHistoryJob extends SpringBeanAwareTestCase {

	@Autowired
	protected HistoryDumpService dumpService;

	@Test
	public void testDumpHistory() throws Throwable {

		dumpService.dumpHistory();
	}
}
