package org.flexpay.ab.sync;

import org.flexpay.ab.service.SyncService;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestSyncAbJob extends SpringBeanAwareTestCase {

	@Autowired
	private SyncService syncService;

	@Test
	public void testSyncAb() throws Throwable {

		syncService.syncAB();
	}
}
