package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * Parent class for all domain objects
 */
public class DomainObject implements Serializable {

    private static final long serialVersionUID = 1L;

	@SuppressWarnings ({"UnusedDeclaration"})
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof DomainObject)) {
			return false;
		}

		DomainObject that = (DomainObject) obj;
		Long thisId = getId();
		// do not check this.id and that.id because of Hibernate proxies that return null
		return thisId != null && that.getId() != null && thisId.equals(that.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", id)
				.append("version", version)
				.toString();
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
