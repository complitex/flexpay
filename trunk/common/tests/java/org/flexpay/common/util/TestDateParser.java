package org.flexpay.common.util;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class TestDateParser {

	@Test
	public void testParseDDMMYYYY() throws Exception {

		DateFormat df = new SimpleDateFormat("ddMMyyyy");
		DateFormat df2 = new SimpleDateFormat("ddMMyyyyHHmmss");

		Date dt1 = new GregorianCalendar(2008, 7, 1).getTime();
		Date dt2 = new GregorianCalendar(2008, 7, 28).getTime();

		assertEquals("Correct date parse failed", dt1, df.parse("01082008"));
		assertEquals("InCorrect date parse failed", dt1, df2.parse("01082008000000"));
		assertEquals("InCorrect 2-d date parse failed", dt2, DateUtil.truncateDay(df2.parse("28082008153317")));
	}
}
