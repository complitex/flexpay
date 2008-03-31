package org.flexpay.ab.sync;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.ab.service.SyncService;

public class TestSyncAbJob extends SpringBeanAwareTestCase {

	@Override
	protected void runTest() throws Throwable {
		testSyncAb();
	}

	public void testSyncAb() throws Throwable {
		SyncService syncService = (SyncService) applicationContext.getBean("abSyncService");

		syncService.syncAB();
	}
}
