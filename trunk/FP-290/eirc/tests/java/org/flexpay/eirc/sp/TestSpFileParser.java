package org.flexpay.eirc.sp;

import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class TestSpFileParser {

	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	int nThreads = 3;
	private CyclicBarrier barrier = new CyclicBarrier(nThreads);
	private final Calendar[] dates = {
			new GregorianCalendar(1900, 1, 1, 0, 0, 1),
			new GregorianCalendar(1111, 11, 11, 11, 12, 12)};

	@Test
	@Ignore
	public void testDateFormat() throws Throwable {
		dateFormat.setLenient(false);
		Formatter[] formatters = {new Formatter(0, "1900/02/01 00:00:01"), new Formatter(1, "1111/12/11 11:12:12")};
		for (int n = 0; n < nThreads - 1; ++n) {
			new Thread(formatters[n]).start();
		}
		barrier.await();
		barrier.await(10, TimeUnit.MILLISECONDS);

		assertEquals("First date year is invalid", 1900, dates[0].get(Calendar.YEAR));
		assertEquals("First date month is invalid", 1, dates[0].get(Calendar.MONTH));
		assertEquals("First date day is invalid", 1, dates[0].get(Calendar.DAY_OF_MONTH));
		assertEquals("First date hour is invalid", 0, dates[0].get(Calendar.HOUR));
		assertEquals("First date minute is invalid", 0, dates[0].get(Calendar.MINUTE));
		assertEquals("First date second is invalid", 1, dates[0].get(Calendar.SECOND));

		assertEquals("Second date year is invalid", 1111, dates[1].get(Calendar.YEAR));
		assertEquals("Second date month is invalid", 11, dates[1].get(Calendar.MONTH));
		assertEquals("Second date day is invalid", 11, dates[1].get(Calendar.DAY_OF_MONTH));
		assertEquals("Second date hour is invalid", 11, dates[1].get(Calendar.HOUR));
		assertEquals("Second date minute is invalid", 12, dates[1].get(Calendar.MINUTE));
		assertEquals("Second date second is invalid", 12, dates[1].get(Calendar.SECOND));
	}

	private class Formatter implements Runnable {
		final private int n;
		final String date;

		private Formatter(int n, String date) {
			this.n = n;
			this.date = date;
		}

		public void run() {
			try {
				barrier.await();
				dates[n].setTime(dateFormat.parse(date));
				barrier.await(10, TimeUnit.MILLISECONDS);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
