package org.flexpay.common.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.ar;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;
import static org.flexpay.common.util.config.ApplicationConfig.getPastInfinite;

public class DateUtil {

	private static final String FLEXPAY_DATE_FORMAT = "yyyy/MM/dd";
	private static final String FLEXPAY_DATE_FORMAT2 = "yyyy-MM-dd";
    private static final String FLEXPAY_DATE_FORMAT_WITH_TIME = "yyyy-MM-dd HH:mm";
	private static final String FLEXPAY_MONTH_FORMAT = "yyyy/MM";

	public static final Map<Integer, String> MONTHS = treeMap(
			ar(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
			ar("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"));

	/**
	 * Check if the date is valid for application
	 *
	 * @param date Date to check
	 * @return <code>true</code> if the date is in application configured past and future infinities
	 */
	public static boolean isValid(@NotNull Date date) {
		return getPastInfinite().compareTo(date) <= 0 &&
			   date.compareTo(getFutureInfinite()) <= 0;
	}

	/**
	 * Parse date in yyyy/MM/dd farmat, if parse fails - return past infinite date
	 *
	 * @param date String in yyyy/MM/dd format, possibly empty
	 * @return Date
	 */
	@NotNull
	public static Date parseBeginDate(String date) {
		return parseDate(date, getPastInfinite());
	}

	/**
	 * Parse date in yyyy/MM/dd farmat, if parse fails - return future infinite date
	 *
	 * @param date String in yyyy/MM/dd format, possibly empty
	 * @return Date
	 */
	@NotNull
	public static Date parseEndDate(String date) {
		return parseDate(date, getFutureInfinite());
	}

	/**
	 * Parse date in yyyy/MM/dd farmat, if parse fails - return default date
	 *
	 * @param date		String in yyyy/MM/dd format, possibly empty
	 * @param defaultDate Default value to return
	 * @return Date
	 */
	@NotNull
	public static Date parseDate(String date, @NotNull Date defaultDate) {
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
	 * Parse date in yyyy/MM/dd or yyyy-MM-dd or yyyy-MM-dd HH:mm formats
	 *
	 * @param date		String in yyyy/MM/dd or yyyy-MM-dd or yyyy-MM-dd HH:mm format, possibly empty
	 * @throws ParseException if parsing fails
	 * @return Date
	 */
	@NotNull
	public static Date parseDate(String date) throws ParseException {
		try {
			return new SimpleDateFormat(FLEXPAY_DATE_FORMAT).parse(date);
		} catch (ParseException ex1) {
			try {
				return new SimpleDateFormat(FLEXPAY_DATE_FORMAT2).parse(date);
			} catch (ParseException ex2) {
				return new SimpleDateFormat(FLEXPAY_DATE_FORMAT_WITH_TIME).parse(date);
			}
		}
	}

	/**
	 * Format Date
	 *
	 * @param date Date to format
	 * @return tring date representation
	 */
	public static String format(@NotNull Date date) {
		SimpleDateFormat df = new SimpleDateFormat(FLEXPAY_DATE_FORMAT);
		return date.equals(getPastInfinite()) ||
			   date.equals(getFutureInfinite())
			   ? "-" : df.format(date);
	}

    /**
     * Format Date
     *
     * @param date Date to format
     * @return tring date representation
     */
    public static String formatWithTime(@NotNull Date date) {
        SimpleDateFormat df = new SimpleDateFormat(FLEXPAY_DATE_FORMAT_WITH_TIME);
        return date.equals(getPastInfinite()) ||
               date.equals(getFutureInfinite())
               ? "-" : df.format(date);
    }

	/**
	 * Format Date
	 *
	 * @param date Date to format
	 * @return tring date representation
	 */
	public static String formatMonth(@NotNull Date date) {
		SimpleDateFormat df = new SimpleDateFormat(FLEXPAY_MONTH_FORMAT);
		return date.equals(getPastInfinite()) ||
			   date.equals(getFutureInfinite())
			   ? "" : df.format(date);
	}

	/**
	 * Return current date
	 *
	 * @return Date with hours, minutes, seconds set to 0
	 */
	@NotNull
	public static Date now() {
		return truncateDay(new Date());
	}

	/**
	 * Return current month
	 *
	 * @return Begining of month date with hours, minutes, seconds set to 0
	 */
	@NotNull
	public static Date currentMonth() {
		return truncateMonth(new Date());
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
		if (dayAfter.compareTo(getFutureInfinite()) > 0) {
			dayAfter = getFutureInfinite();
		}

		return dayAfter;
	}

	/**
	 * Get month date following the specified <code>date</code>'s month
	 *
	 * @param date Date to get the next month for
	 * @return Next month date
	 */
	@NotNull
	public static Date nextMonth(@NotNull Date date) {
		Date dayAfter = DateUtils.addMonths(truncateMonth(date), 1);
		if (dayAfter.compareTo(getFutureInfinite()) > 0) {
			dayAfter = getFutureInfinite();
		}

		return dayAfter;
	}

	/**
	 * Get date that date is the same as of <code>dt</code>, but hours, minutes, seconds and millis are 0
	 *
	 * @param dt Date to truncate
	 * @return Date that hours, minuts, seconds and millis are all 0
	 */
	@NotNull
	public static Date truncateDay(@NotNull Date dt) {
		return DateUtils.truncate(dt, Calendar.DAY_OF_MONTH);
	}

	/**
	 * Get date that month is the me as of <code>dt</code>, but day is the first and hours, minutes, seconds and millis are
	 * 0
	 *
	 * @param dt Date to truncate
	 * @return Date that hours, minuts, seconds and millis are all 0
	 */
	@NotNull
	public static Date truncateMonth(@NotNull Date dt) {
		return DateUtils.truncate(dt, Calendar.MONTH);
	}

	/**
	 * Get date preciding the specified <code>date</code>
	 *
	 * @param date Date to get the next for
	 * @return Next day date
	 */
	@NotNull
	public static Date previous(@NotNull Date date) {
		Date dayBefore = DateUtils.addDays(date, -1);
		if (dayBefore.compareTo(getPastInfinite()) < 0) {
			dayBefore = getPastInfinite();
		}

		return dayBefore;
	}

	/**
	 * Get month date preciding the specified <code>date</code>'s month
	 *
	 * @param date Date to get the previous month for
	 * @return Previous month date
	 */
	@NotNull
	public static Date previousMonth(@NotNull Date date) {
		Date dayBefore = DateUtils.addMonths(truncateMonth(date), -1);
		if (dayBefore.compareTo(getPastInfinite()) < 0) {
			dayBefore = getPastInfinite();
		}

		return dayBefore;
	}

	/**
	 * Returns date which has the same day to given but time is 23:59:59
	 * @param date date
	 * @return date which has the same day to given but time is 23:59:59
	 */
	@NotNull
	public static Date getEndOfThisDay(@NotNull Date date) {
		Date result = DateUtil.truncateDay(date);
		result = DateUtils.setHours(result, 23);
		result = DateUtils.setMinutes(result, 59);
		result = DateUtils.setSeconds(result, 59);
		return result;
	}
}
