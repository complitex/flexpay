package org.flexpay.common.persistence;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Parent class for all domain objects
 */
public class DomainObject implements Serializable {

	@SuppressWarnings({"UnusedDeclaration"})
	protected Integer version;
	private Long id;

	/**
	 * Constructs a new DomainObject.
	 */
	public DomainObject() {
	}

	public DomainObject(@NotNull Long id) {
		this.id = id;
	}

	/**
	 * Getter for property 'id'.
	 *
	 * @return Value for property 'id'.
	 */
	@Nullable
	public Long getId() {
		return id;
	}

	/**
	 * Setter for property 'id'.
	 *
	 * @param id Value to set for property 'id'.
	 */
	public void setId(@Nullable Long id) {
		this.id = id;
	}

	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

	/**
	 * Check if object is new (not persistent instance)
	 *
	 * @return <code>true</code> if object id is null or equals to 0, or <code>false</code> otherwise
	 */
	public boolean isNew() {
		return id == null || id <= 0;
	}

	public boolean isNotNew() {
		return !isNew();
	}
}
