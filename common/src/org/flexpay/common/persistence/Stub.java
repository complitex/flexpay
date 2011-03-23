package org.flexpay.common.persistence;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Stub is a simple holder of DomainObject ids
 */
public class Stub<T extends DomainObject> implements Serializable {

	private static final String INVALID_STUB_ID = "Invalid stub id: ";

	// object id
	private Long id;

	public Stub(@NotNull T o) {
		//noinspection ConstantConditions
		this(o.getId());
	}

	public Stub(@NotNull Long id) {
		Validate.isTrue(id > 0, INVALID_STUB_ID + id);

		this.id = id;
	}

	@NotNull
	public Long getId() {
		return id;
	}

	public static <T extends DomainObject> Stub<T> stub(@NotNull T o) {
		return new Stub<T>(o);
	}

	public boolean sameId(@NotNull T o) {
		return id.equals(o.getId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Stub)) {
			return false;
		}

		Stub<?> stub = (Stub<?>) o;

		return id.equals(stub.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", id)
				.toString();
	}

}
