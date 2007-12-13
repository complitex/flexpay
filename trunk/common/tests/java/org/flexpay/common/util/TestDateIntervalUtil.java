package org.flexpay.common.util;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertEquals;
import org.flexpay.common.persistence.DateInterval;
import static org.flexpay.common.util.DateIntervalUtil.areIntersect;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;

public class TestDateIntervalUtil {

	@Test
	public void testDateFormat() throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date dt = sdf.parse("2007/11/10");

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(dt);

		assertEquals("Year is invalid", 2007, calendar.get(Calendar.YEAR));
		assertEquals("Month is invalid", 11, calendar.get(Calendar.MONTH) + 1);
		assertEquals("Day is invalid", 10, calendar.get(Calendar.DAY_OF_MONTH));
	}

	@Test
	public void testAreIntersect() {

		assertTrue("Infinit intervals are not intersecting", areIntersect(
				new DateInterval(null, null),
				new DateInterval(null, null)));
		assertFalse("Infinit intervals are intersecting", areIntersect(
				new DateInterval(null, new Date(0)),
				new DateInterval(new Date(1), null)));
	}
}
