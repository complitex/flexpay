package org.flexpay.common.util;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class TestNotNull {

	@Test(expected = IllegalStateException.class)
	public void testCheckNullReturnType() {
		getNullInt();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckNotNullParameter() {
		//noinspection ConstantConditions
		callNotNullInt(null);
	}

	@NotNull
	private Integer getNullInt() {
		//noinspection ConstantConditions
		return null;
	}

	private Integer callNotNullInt(@NotNull Integer i) {
		return i;
	}
}
