package org.flexpay.common.process.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Pavel Sknar
 *         Date: 20.10.11 11:36
 */
public class WorkItemCompleteLocker {
	private static final Logger log = LoggerFactory.getLogger(WorkItemCompleteLocker.class);
	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public static void lock() {
		Thread th = Thread.currentThread();
		log.debug("lock thread {} ({})", th.getId(), th.getStackTrace()[2]);
		lock.writeLock().lock();
		log.debug("locked thread {}", th.getId());
	}

	public static void unlock() {
		Thread th = Thread.currentThread();
		log.debug("unlock thread {}", th.getId());
		lock.writeLock().unlock();
		log.debug("unlocked thread {}", th.getId());
	}
}
