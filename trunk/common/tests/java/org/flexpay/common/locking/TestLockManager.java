package org.flexpay.common.locking;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Repeat;

import static org.junit.Assert.*;

public class TestLockManager extends SpringBeanAwareTestCase {

	public static final String lockString = "lock String";

	@Autowired
	public LockManager lockManager;

	@Before
	public void setUp() throws Exception {
		lockManager.releaseLock(lockString);
	}

	@Test
	public void testLockManagerSimple() {
		String resource = "testLockManagerSimple";
		try {
			assertTrue("Failed getting lock", lockManager.lock(resource));
		} finally {
			lockManager.releaseLock(resource);
		}
	}

	@Test (timeout = 2500)
	@Repeat (5)
	public void testLock() {

		assertTrue("lock string", lockManager.lock(lockString));
		ConflictingThread conflictingThread = new ConflictingThread();
		Thread runner = new Thread(conflictingThread);
		runner.start();

		Logger log = LoggerFactory.getLogger(TestLockManager.class);

		try {
			runner.join();
		} catch (InterruptedException e) {
			log.debug("Interrupted", e);
			fail("Interrupted!");
		}

		assertFalse(conflictingThread.locked);
		lockManager.releaseLock(lockString);

		runner = new Thread(conflictingThread);
		runner.start();

		try {
			runner.join();
		} catch (InterruptedException e) {
			log.debug("Interrupted!", e);
			fail("Interrupted!");
		}

		assertTrue(conflictingThread.locked);
		lockManager.releaseLock(lockString);
	}


	public class ConflictingThread implements Runnable {
		public boolean locked = false;

		public void run() {
			locked = lockManager.lock(lockString);
		}
	}
}
