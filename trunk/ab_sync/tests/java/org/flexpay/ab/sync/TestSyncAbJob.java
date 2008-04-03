package org.flexpay.ab.sync;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.locking.LockManager;
import org.flexpay.ab.service.SyncService;

public class TestSyncAbJob extends SpringBeanAwareTestCase {

	@Override
	protected void runTest() throws Throwable {
		testSyncAb();
	}

	public void testSyncAb() throws Throwable {
		LockManager lockManager = (LockManager) applicationContext.getBean("LockManager");
		if (!lockManager.lock("sync_ab_lock")) {
			// another process already requested a lock and is working
			fail("Lock asquisition failed.");
		}

		SyncService syncService = (SyncService) applicationContext.getBean("abSyncService");

		syncService.syncAB();

		lockManager.releaseLock("sync_ab_lock");
	}
}
