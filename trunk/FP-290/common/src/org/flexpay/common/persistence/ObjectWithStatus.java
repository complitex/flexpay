package org.flexpay.common.persistence;

public interface ObjectWithStatus {

	/**
	 * Active object status
	 */
	int STATUS_ACTIVE = 0;

	/**
	 * Disabled object status
	 */
	int STATUS_DISABLED = 1;

	/**
	 * Getter for property 'status'.
	 *
	 * @return Value for property 'status'.
	 */
	int getStatus();

	/**
	 * Setter for property 'status'.
	 *
	 * @param status Value to set for property 'status'.
	 */
	void setStatus(int status);
}
