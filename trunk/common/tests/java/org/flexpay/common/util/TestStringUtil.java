package org.flexpay.common.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestStringUtil {

	@Test
	public void testEscapableRemoved() {
		List<String> parts = StringUtil.splitEscapable("lkjhsalkdjfh/ sadfsa df", ' ', '/');
		assertTrue("Escape symbol not removed", parts.get(0).indexOf('/') == -1);
	}

	@Test
	public void testSplitEmpty() {
		assertEquals("Escape symbol not removed", 0, StringUtil.splitEscapable("", ' ', '/').size());
	}

	@Test
	public void testEscapeNotDelimiterEmpty() {

		List<String> parts = StringUtil.splitEscapable("sadgf/,asdf/ sadf asfd", ' ', '/');
		assertEquals("Invalid split realisation", 2, parts.size());
		assertEquals("Invalid escape handling", "sadgf/,asdf sadf", parts.get(0));
		assertEquals("Invalid splitting", "asfd", parts.get(1));
	}

	@Test (expected = IllegalArgumentException.class)
	public void testSplitEscapable() {
		StringUtil.splitEscapable("kjhsdfgkljhsdfg/", 'k', '/');
	}

	@Test
	public void testSplitSimple() {
		List<String> parts = StringUtil.splitEscapable("lkjhsalkdjfh sadfsa df", ' ', '/');
		assertEquals("Invalid split realisation: ", 3, parts.size());
		assertEquals("invalid first part", "lkjhsalkdjfh", parts.get(0));
		assertEquals("invalid second part", "sadfsa", parts.get(1));
		assertEquals("invalid third part", "df", parts.get(2));
	}

	@Test
	public void testSplitFinalDelimiter() {
		List<String> parts = StringUtil.splitEscapable("lkjhsalkdjfh df ", ' ', '/');
		assertEquals("Invalid split realisation: ", 3, parts.size());
		assertEquals("invalid first part", "lkjhsalkdjfh", parts.get(0));
		assertEquals("invalid third part", "df", parts.get(1));
		assertEquals("invalid third part", "", parts.get(2));
	}

	@Test
	public void testSplitEscapableSimple() {
		List<String> parts = StringUtil.splitEscapable("lkjhsalkdjfh/ sadfsa df", ' ', '/');
		assertEquals("Invalid split realisation (escaped delimiter): ", 2, parts.size());
		assertEquals("invalid first part", "lkjhsalkdjfh sadfsa", parts.get(0));
		assertEquals("invalid second part", "df", parts.get(1));
	}

	@Test
	public void testGetDigit() {
		BigDecimal bd = new BigDecimal("12.43");

		assertEquals("Invalid digit 1", "0", StringUtil.getDigit(bd, 2));
		assertEquals("Invalid digit 1", "1", StringUtil.getDigit(bd, 1));
		assertEquals("Invalid digit 1", "2", StringUtil.getDigit(bd, 0));
		assertEquals("Invalid digit 1", "4", StringUtil.getDigit(bd, -1));
		assertEquals("Invalid digit 1", "3", StringUtil.getDigit(bd, -2));
		assertEquals("Invalid digit 1", "0", StringUtil.getDigit(bd, -3));

		assertEquals("Invalid digit 1", "0", StringUtil.getDigit(BigDecimal.ZERO, -3));
		assertEquals("Invalid digit 1", "0", StringUtil.getDigit(BigDecimal.ZERO, 0));
		assertEquals("Invalid digit 1", "0", StringUtil.getDigit(BigDecimal.ZERO, 123));
	}

	@Test
	public void testGetFileName() {
		String fName = "/org/flexpay/bti/service/importexport/26.12.2008.csv";
		assertEquals("invalid getFileName ", "26.12.2008.csv", StringUtil.getFileName(fName));
		assertEquals("invalid getExtension ", ".csv", StringUtil.getFileExtension(fName));
	}
}
