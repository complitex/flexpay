package org.flexpay.common.drools.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WorkItemCompleteLocker {
	private static final Logger log = LoggerFactory.getLogger(WorkItemCompleteLocker.class);
	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public static void lock() {
		log.debug("lock thread {}", Thread.currentThread().getId());
		lock.writeLock().lock();
		log.debug("locked thread {}", Thread.currentThread().getId());
	}

	public static void unlock() {
		log.debug("unlock thread {}", Thread.currentThread().getId());
		lock.writeLock().unlock();
		log.debug("unlocked thread {}", Thread.currentThread().getId());
	}
}
