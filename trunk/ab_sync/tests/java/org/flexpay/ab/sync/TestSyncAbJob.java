package org.flexpay.ab.sync;

import org.flexpay.ab.service.SyncService;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;

public class TestSyncAbJob extends SpringBeanAwareTestCase {

	@Test
	public void testSyncAb() throws Throwable {

		SyncService syncService = (SyncService) applicationContext.getBean("abSyncService");
		syncService.syncAB();
	}
}
