package org.flexpay.ab.sync;

import org.flexpay.ab.service.HistoryDumpService;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.NotTransactional;

public class TestDumpHistoryJob extends SpringBeanAwareTestCase {

	@Autowired
	protected HistoryDumpService dumpService;

	@Test
	@NotTransactional
	public void testDumpHistory() throws Throwable {

		dumpService.dumpHistory();
	}
}
