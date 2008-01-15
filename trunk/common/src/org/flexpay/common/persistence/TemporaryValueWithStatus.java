package org.flexpay.common.persistence;

public abstract class TemporaryValueWithStatus<T extends TemporaryValue>
		extends TemporaryValue<T> implements ObjectWithStatus {

	private int status;

	/**
	 * Constructs a new TemporaryValueWithStatus.
	 */
	public TemporaryValueWithStatus() {
	}

	/**
	 * {@inheritDoc}
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setStatus(int status) {
		this.status = status;
	}
}
