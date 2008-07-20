package org.flexpay.common.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.Calendar;

public class DateUtil {

	@NonNls
	private static final String FLEXPAY_DATE_FORMAT = "yyyy/MM/dd";
	public static Map<Integer, String> MONTHS;

	static {
		MONTHS = new TreeMap<Integer, String>();
		MONTHS.put(0, "01");
		MONTHS.put(1, "02");
		MONTHS.put(2, "03");
		MONTHS.put(3, "04");
		MONTHS.put(4, "05");
		MONTHS.put(5, "06");
		MONTHS.put(6, "07");
		MONTHS.put(7, "08");
		MONTHS.put(8, "09");
		MONTHS.put(9, "10");
		MONTHS.put(10, "11");
		MONTHS.put(11, "12");
	}

	/**
	 * Check if the date is valid for application
	 *
	 * @param date Date to check
	 * @return <code>true</code> if the date is in application configured past and future infinities
	 */
	public static boolean isValid(Date date) {
		return ApplicationConfig.getPastInfinite().compareTo(date) <= 0 &&
			   date.compareTo(ApplicationConfig.getFutureInfinite()) <= 0;
	}

	/**
	 * Parse date in yyyy/MM/dd farmat, if parse fails - return past infinite date
	 *
	 * @param date String in yyyy/MM/dd format, possibly empty
	 * @return Date
	 */
	public static Date parseBeginDate(String date) {
		return parseDate(date, ApplicationConfig.getPastInfinite());
	}

	/**
	 * Parse date in yyyy/MM/dd farmat, if parse fails - return future infinite date
	 *
	 * @param date String in yyyy/MM/dd format, possibly empty
	 * @return Date
	 */
	public static Date parseEndDate(String date) {
		return parseDate(date, ApplicationConfig.getFutureInfinite());
	}

	/**
	 * Parse date in yyyy/MM/dd farmat, if parse fails - return default date
	 *
	 * @param date		String in yyyy/MM/dd format, possibly empty
	 * @param defaultDate Default value to return
	 * @return Date
	 */
	public static Date parseDate(String date, Date defaultDate) {
		if (StringUtils.isBlank(date)) {
			return defaultDate;
		}

		try {
			return new SimpleDateFormat(FLEXPAY_DATE_FORMAT).parse(date);
		} catch (ParseException e) {
			return defaultDate;
		}
	}

	/**
	 * Format Date
	 *
	 * @param date Date to format
	 * @return tring date representation
	 */
	public static String format(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(FLEXPAY_DATE_FORMAT);
		return date.equals(ApplicationConfig.getPastInfinite()) ||
			   date.equals(ApplicationConfig.getFutureInfinite())
			   ? "-" : df.format(date);
	}

	/**
	 * Return current date
	 *
	 * @return Date with hours, minutes, seconds set to 0
	 */
	@NotNull
	public static Date now() {
		return DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
	}

	/**
	 * Return current month
	 *
	 * @return Begining of month date with hours, minutes, seconds set to 0
	 */
	@NotNull
	public static Date currentMonth() {
		return DateUtils.truncate(new Date(), Calendar.MONTH);
	}

	/**
	 * Get date following the specified <code>date</code>
	 *
	 * @param date Date to get the next for
	 * @return Next day date
	 */
	@NotNull
	public static Date next(@NotNull Date date) {
		Date dayAfter = DateUtils.addDays(date, 1);
		if (dayAfter.compareTo(ApplicationConfig.getFutureInfinite()) > 0) {
			dayAfter = ApplicationConfig.getFutureInfinite();
		}

		return dayAfter;
	}

	/**
	 * Get date following the specified <code>date</code>
	 *
	 * @param date Date to get the next for
	 * @return Next day date
	 */
	@NotNull
	public static Date previous(@NotNull Date date) {
		Date dayBefore = DateUtils.addDays(date, -1);
		if (dayBefore.compareTo(ApplicationConfig.getPastInfinite()) < 0) {
			dayBefore = ApplicationConfig.getPastInfinite();
		}

		return dayBefore;
	}
}
