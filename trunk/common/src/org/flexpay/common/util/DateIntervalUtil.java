package org.flexpay.common.util;

import org.flexpay.common.persistence.DateInterval;

import java.util.Collection;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DateIntervalUtil {

	/**
	 * Check if two date interval intersect
	 *
	 * @param di1 the first Date interval
	 * @param di2 the second date interval
	 * @return <code>true</code> if intervals are intersecting, or <code>false</code>
	 *         otherwise
	 */
	public static boolean areIntersect(DateInterval di1, DateInterval di2) {

		Date begin1 = di1.getBegin();
		Date end1 = di1.getEnd();
		Date begin2 = di2.getBegin();
		Date end2 = di2.getEnd();

		// Simple cases, pairs of infinit intervals
		if (begin1 == null && end1 == null) {
			return true;
		} else if (begin2 == null && end2 == null) {
			return true;
		} else if (begin1 == null && begin2 == null) {
			return true;
		} else if (end1 == null && end2 == null) {
			return true;
		}

		// One of the intervals is infinit
		if (begin1 == null) {
			return end1.compareTo(begin2) > 0;
		} else if (begin2 == null) {
			return end2.compareTo(begin1) > 0;
		} else if (end1 == null) {
			return begin1.compareTo(end2) < 0;
		} else if (end2 == null) {
			return begin2.compareTo(end1) < 0;
		}

		if (begin1.compareTo(begin2) < 0) {
			return end1.compareTo(begin2) > 0;
		} else {
			return end2.compareTo(begin1) > 0;
		}
	}

	/**
	 * Check if the first interval is laying inside the second
	 *
	 * @param di1 the first interval
	 * @param di2 the second interval
	 * @return <code>true</code> if the first lays in second one, or <code>false</code>
	 *         otherwise
	 */
	public static boolean isInside(DateInterval di1, DateInterval di2) {

		Date begin1 = di1.getBegin();
		Date end1 = di1.getEnd();
		Date begin2 = di2.getBegin();
		Date end2 = di2.getEnd();

		return (begin2 == null || (begin1 != null && begin1.compareTo(begin2) >= 0)) &&
			   (end2 == null || (end1 != null && end1.compareTo(end2) <= 0));
	}

	/**
	 * Find interval that lays in specified <code>dateInterval</code>
	 *
	 * @param intervals	Collection of interval candidates
	 * @param dateInterval Interval to find candidate in
	 * @return DateInterval from collection that lays inside the specified one, or
	 *         <code>null</code> if not found
	 */
	public static DateInterval getInterval(
			Collection<? extends DateInterval> intervals, DateInterval dateInterval) {

		for (DateInterval di : intervals) {
			if (areIntersect(di, dateInterval)) {
				return di;
			}
		}

		return null;
	}

	/**
	 * Check if DateInterval is valid, i.e. its begin date is before the end date
	 *
	 * @param di DateInterval to check
	 * @return <code>true</code> if interval is valid, or <code>false</code> otherwise
	 */
	public static boolean isValid(DateInterval di) {
		return di.getBegin() == null || di.getEnd() == null ||
			   di.getBegin().compareTo(di.getEnd()) <= 0;
	}

	/**
	 * Format DateInterval begin and end date values
	 *
	 * @param di DateInterval
	 * @return Array of two strings date representations
	 */
	public static String[] format(DateInterval di) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		return new String[] {
				di.getBegin() != null ? df.format(di.getBegin()) : "-",
				di.getEnd() != null ? df.format(di.getEnd()) : "-",
		};
	}
}
