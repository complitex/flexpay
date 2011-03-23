package org.flexpay.ab.sync;

import org.flexpay.ab.service.SyncService;
import org.flexpay.ab.test.AbSpringBeanAwareTestCase;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestSyncAbJob extends AbSpringBeanAwareTestCase {

	@Autowired
	private SyncService syncService;

	@Test
	public void testSyncAb() throws Throwable {

//		Number notProcessedCount = (Number) jdbcTemplate.queryForObject(
//				"select count(1) from ab_sync_changes_tbl where processed=0", Number.class);
//		assertNotSame("Should have not processed data", 0, notProcessedCount.intValue());

		syncService.syncAB();

//		notProcessedCount = (Number) jdbcTemplate.queryForObject(
//				"select count(1) from ab_sync_changes_tbl where processed=0", Number.class);
//		assertEquals("Should not have not processed data", 0, notProcessedCount.intValue());
	}

	@After
	public void cleanup() {
//		jdbcTemplate.update("update ab_sync_changes_tbl set processed=0");
	}
}
