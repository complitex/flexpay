package org.flexpay.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

import java.util.List;

public class TestStringUtil {

	@Test
	public void testSplitEscapable() {
		List<String> parts1 = StringUtil.splitEscapable("lkjhsalkdjfh sadfsa df", ' ', '/');
		assertEquals("Invalid split realisation: ", 3, parts1.size());

		List<String> parts2 = StringUtil.splitEscapable("lkjhsalkdjfh/ sadfsa df", ' ', '/');
		assertEquals("Invalid split realisation (escaped delimiter): ", 2, parts2.size());

		try {
			StringUtil.splitEscapable("kjhsdfgkljhsdfg/", 'k', '/');
			fail("Not caught final escape character");
		} catch (IllegalArgumentException e) {
			// well done
		}
	}
}
