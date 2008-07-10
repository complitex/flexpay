package org.flexpay.common.persistence;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

/**
 * Stub is a simple holder of DomainObject ids
 */
public class Stub<T extends DomainObject> {

	// object id
	private Long id;

	public Stub(T o) {
		Validate.notNull(o.getId(), "Nulls are not accepted");
		//noinspection ConstantConditions
		Validate.isTrue(o.getId() > 0, "Stub id should be > 0");

		this.id = o.getId();
	}

	@NotNull
	public Long getId() {
		return id;
	}

	public static <T extends DomainObject> Stub<T> stub(T o) {
		return new Stub<T>(o);
	}
}
