package org.flexpay.common.persistence;

import java.io.Serializable;

/**
 * Parent class for all domain objects
 */
public class DomainObject implements Serializable {

	private Long id;

	/**
	 * Constructs a new DomainObject.
	 */
	public DomainObject() {
	}

	public DomainObject(Long id) {
		this.id = id;
	}

	/**
	 * Getter for property 'id'.
	 *
	 * @return Value for property 'id'.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter for property 'id'.
	 *
	 * @param id Value to set for property 'id'.
	 */
	public void setId(Long id) {
		this.id = id;
	}
}
