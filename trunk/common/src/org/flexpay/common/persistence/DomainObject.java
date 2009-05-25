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

	public DomainObject() {
	}

	public DomainObject(@NotNull Long id) {
		this.id = id;
	}

	@Nullable
	public Long getId() {
		return id;
	}

	public void setId(@Nullable Long id) {
		this.id = id;
	}

	public boolean isNew() {
		return id == null || id <= 0;
	}

	public boolean isNotNew() {
		return !isNew();
	}

	@Override
	public int hashCode() {
		return id == null ? super.hashCode() : id.hashCode();
	}

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

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", id)
				.append("version", version)
				.toString();
	}

}
