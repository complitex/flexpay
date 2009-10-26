package org.flexpay.ab.sync;

import org.flexpay.ab.service.SyncService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestSyncAbJob extends AbSpringBeanAwareTestCase {

	@Autowired
	private SyncService syncService;

	@Test
	public void testSyncAb() throws Throwable {

		syncService.syncAB();
	}
}
