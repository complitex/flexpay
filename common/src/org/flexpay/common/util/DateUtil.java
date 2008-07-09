package org.flexpay.common.util;

import org.flexpay.common.util.config.ApplicationConfig;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class DateUtil {

	public static Map<Integer, String> MONTHS;
	public static Integer[] YEARS;
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

		Calendar cal = Calendar.getInstance();
		cal.setTime(ApplicationConfig.getPastInfinite());
		int yearFrom = cal.get(Calendar.YEAR);
		cal.setTime(ApplicationConfig.getFutureInfinite());
		int yearTill = cal.get(Calendar.YEAR);
		YEARS = new Integer[yearTill - yearFrom + 1];
		for (int i = 0; i <= yearTill - yearFrom; i++) {
			YEARS[i] = yearFrom + i;
		}
	}
	
	public static String format(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
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
	 * @param date String in yyyy/MM/dd format, possibly empty
	 * @param defaultDate Default value to return
	 * @return Date
	 */
	public static Date parseDate(String date, Date defaultDate) {
		if (StringUtils.isBlank(date)) {
			return defaultDate;
		}

		try {
			return new SimpleDateFormat("yyyy/MM/dd").parse(date);
		} catch (ParseException e) {
			return defaultDate;
		}
	}
}
