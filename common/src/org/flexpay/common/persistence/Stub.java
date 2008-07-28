package org.flexpay.common.persistence;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;

/**
 * Stub is a simple holder of DomainObject ids
 */
public class Stub<T extends DomainObject> {

	@NonNls
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
}
