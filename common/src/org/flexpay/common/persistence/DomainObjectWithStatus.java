package org.flexpay.common.persistence;

public class DomainObjectWithStatus extends DomainObject implements ObjectWithStatus {

	private int status = ObjectWithStatus.STATUS_ACTIVE;

	/**
	 * Constructs a new DomainObject.
	 */
	public DomainObjectWithStatus() {
	}

	public DomainObjectWithStatus(Long id) {
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
}
