package org.flexpay.eirc.util;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TestBigDecimal {

	@Test
	public void testParse1() {
		BigDecimal num = new BigDecimal("123.22");
		assertEquals("Invalid number", "123.22", num.toString());
	}

	@Test
	public void testParse3AfterDot() {
		BigDecimal num = new BigDecimal("123.223");
		assertEquals("Invalid number", "123.223", num.toString());
	}
}
