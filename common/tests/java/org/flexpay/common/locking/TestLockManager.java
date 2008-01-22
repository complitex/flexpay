package org.flexpay.common.locking;

import junit.framework.TestCase;

import org.flexpay.common.logger.FPLogger;

public class TestLockManager extends TestCase {
    public static final String lockString = "lock String";

    protected void setUp() throws Exception {
        LockManager lockManager = LockManager.getInstance();
        lockManager.releaseLock(lockString);
    }

    public void testLock() {
        LockManager lockManager = LockManager.getInstance();
        assertTrue("Lock string", lockManager.lock(lockString));
        ConflictingThread conflictingThread = new ConflictingThread();
        Thread runner = new Thread(conflictingThread);
        runner.start();

        try {
            runner.join();
        } catch (InterruptedException e) {
            FPLogger.logMessage(FPLogger.DEBUG, "TestLockManager: testLock: interrupted!", e);
            fail();
        }

        assertFalse(conflictingThread.locked);
        lockManager.releaseLock(lockString);

        runner = new Thread(conflictingThread);
        runner.start();

        try {
            runner.join();
        } catch (InterruptedException e) {
            FPLogger.logMessage(FPLogger.DEBUG, "TestLockManager: testLock: interrupted!", e);
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

