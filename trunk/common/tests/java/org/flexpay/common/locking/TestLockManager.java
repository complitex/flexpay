package org.flexpay.common.locking;

import org.apache.log4j.Logger;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Repeat;

public class TestLockManager extends SpringBeanAwareTestCase {

	public static final String lockString = "lock String";

	@Before
	public void setUp() throws Exception {
		LockManager lockManager = LockManager.getInstance();
		lockManager.releaseLock(lockString);
	}

	@Test
	@Repeat(50)
	public void testLock() {
		LockManager lockManager = LockManager.getInstance();
		assertTrue("lock string", lockManager.lock(lockString));
		ConflictingThread conflictingThread = new ConflictingThread();
		Thread runner = new Thread(conflictingThread);
		runner.start();

		Logger log = Logger.getLogger(TestLockManager.class);

		try {
			runner.join();
		} catch (InterruptedException e) {
			log.debug("TestLockManager: testLock: interrupted!", e);
			fail("TestLockManager: testLock: interrupted!");
		}

		assertFalse(conflictingThread.locked);
		lockManager.releaseLock(lockString);

		runner = new Thread(conflictingThread);
		runner.start();

		try {
			runner.join();
		} catch (InterruptedException e) {
			log.debug("TestLockManager: testLock: interrupted!", e);
			fail();
		}
		assertTrue(conflictingThread.locked);
		lockManager.releaseLock(lockString);
	}


	public class ConflictingThread implements Runnable {
		public boolean locked = false;

		public void run() {
			LockManager lockManager = LockManager.getInstance();
			locked = lockManager.lock(lockString);
		}
	}
}
