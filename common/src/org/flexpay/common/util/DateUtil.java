package org.flexpay.common.util;

import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Date;

public class DateUtil {

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
