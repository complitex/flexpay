package org.flexpay.common.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestIntegerUtil {

	@Test
	public void testParseColumnName() {

		assertEquals("A parse failed", 1, IntegerUtil.parseXLSInt("A"));
		assertEquals("Z parse failed", 26, IntegerUtil.parseXLSInt("Z"));

		assertEquals("AA parse failed", 27, IntegerUtil.parseXLSInt("AA"));
	}

	@Test
	public void testGenerateColumnName() {

		assertEquals("A generation failed", "A", IntegerUtil.toXLSColumnNumber(1));
		assertEquals("Z generation failed", "Z", IntegerUtil.toXLSColumnNumber(26));

		assertEquals("AA generation failed", "AA", IntegerUtil.toXLSColumnNumber(27));
	}
}
