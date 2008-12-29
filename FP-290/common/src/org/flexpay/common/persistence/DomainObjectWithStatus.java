package org.flexpay.common.persistence;

import org.jetbrains.annotations.NotNull;

public class DomainObjectWithStatus extends DomainObject implements ObjectWithStatus {

	private int status = ObjectWithStatus.STATUS_ACTIVE;

	/**
	 * Constructs a new DomainObject.
	 */
	public DomainObjectWithStatus() {
	}

	public DomainObjectWithStatus(@NotNull Long id) {
		super(id);
	}

	/**
	 * Getter for property 'status'.
	 *
	 * @return Value for property 'status'.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Setter for property 'status'.
	 *
	 * @param status Value to set for property 'status'.
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Set current object status to disabled
	 */
	public void disable() {
		status = STATUS_DISABLED;
	}

	/**
	 * Check if object is active
	 *
	 * @return <code>true</code> if object status is active, or <code>false</code> otherwise
	 */
	public boolean isActive() {
		return status == STATUS_ACTIVE;
	}
}
