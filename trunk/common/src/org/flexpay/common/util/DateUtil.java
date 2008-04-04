package org.flexpay.common.util;

import org.flexpay.common.util.config.ApplicationConfig;

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
		cal.setTime(ApplicationConfig.getInstance().getPastInfinite());
		int yearFrom = cal.get(Calendar.YEAR);
		cal.setTime(ApplicationConfig.getInstance().getFutureInfinite());
		int yearTill = cal.get(Calendar.YEAR);
		YEARS = new Integer[yearTill - yearFrom + 1];
		for (int i = 0; i <= yearTill - yearFrom; i++) {
			YEARS[i] = yearFrom + i;
		}
	}
	
	
	
	/**
	 * Check if the date is valid for application
	 *
	 * @param date Date to check
	 * @return <code>true</code> if the date is in application configured past and future infinities
	 */
	public static boolean isValid(Date date) {
		return ApplicationConfig.getInstance().getPastInfinite().compareTo(date) <= 0 &&
			   date.compareTo(ApplicationConfig.getInstance().getFutureInfinite()) <= 0;
	}
}
