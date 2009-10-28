package org.flexpay.ab.sync;

import org.flexpay.ab.service.HistoryDumpService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestDumpHistoryJob extends AbSpringBeanAwareTestCase {

	@Autowired
	protected HistoryDumpService dumpService;

	@Test
	public void testDumpHistory() throws Throwable {

		dumpService.dumpHistory();
	}
}
