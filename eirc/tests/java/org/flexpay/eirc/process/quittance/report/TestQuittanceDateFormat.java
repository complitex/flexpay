package org.flexpay.eirc.process.quittance.report;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TestQuittanceDateFormat {

	@Test
	public void testDateGeneration() {

		DateFormat df = new SimpleDateFormat("MM/yyyy");

		Calendar cal = new GregorianCalendar(2008, 11, 1);
		assertEquals("Invalid date format", "12/2008", df.format(cal.getTime()));

		cal = new GregorianCalendar(2007, 0, 1);
		assertEquals("Invalid date format", "01/2007", df.format(cal.getTime()));
	}

	@Test
	public void testDateGeneration2() {

		DateFormat df = new SimpleDateFormat("MMyyyy");

		Calendar cal = new GregorianCalendar(2008, 11, 1);
		assertEquals("Invalid date format", "122008", df.format(cal.getTime()));

		cal = new GregorianCalendar(2007, 0, 1);
		assertEquals("Invalid date format", "012007", df.format(cal.getTime()));
	}
}
