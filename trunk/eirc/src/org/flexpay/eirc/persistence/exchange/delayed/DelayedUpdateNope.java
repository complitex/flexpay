package org.flexpay.eirc.persistence.exchange.delayed;

import org.flexpay.eirc.persistence.exchange.DelayedUpdate;

/**
 * Delayed update that does nothing
 */
public class DelayedUpdateNope implements DelayedUpdate {

	// shared instance
	public static final DelayedUpdate INSTANCE = new DelayedUpdateNope();

	// private constructor
	private DelayedUpdateNope() {
	}

	/**
	 * Perform storage update
	 */
	@Override
	public void doUpdate() {
		// do nothing
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this == INSTANCE && obj instanceof DelayedUpdateNope;
	}
}
