package org.flexpay.common.process.jpa;

public interface LockedManager {

	void lock();

	void unlock();
}
