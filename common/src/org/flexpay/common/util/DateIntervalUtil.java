package org.flexpay.common.util;

import org.flexpay.common.persistence.DateInterval;
import org.flexpay.common.persistence.Pair;
import org.flexpay.common.persistence.TemporaryValue;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DateIntervalUtil {

	/**
	 * Check if two date intervals intersect
	 *
	 * @param di1 the first Date interval
	 * @param di2 the second date interval
	 * @return <code>true</code> if intervals are intersecting, or <code>false</code> otherwise
	 */
	public static boolean areIntersecting(DateInterval<?, ?> di1, DateInterval<?, ?> di2) {
		return !(di1.getEnd().compareTo(di2.getBegin()) < 0
				 || di1.getBegin().compareTo(di2.getEnd()) > 0);
	}

	/**
	 * Check if two date intervals intersect
	 *
	 * @param i1Begin the first Date interval begin date
	 * @param i1End   the first Date interval end date
	 * @param i2Begin the second Date interval begin date
	 * @param i2End   the second Date interval end date
	 * @return <code>true</code> if intervals are intersecting, or <code>false</code> otherwise
	 */
	public static boolean areIntersecting(Date i1Begin, Date i1End, Date i2Begin, Date i2End) {
		return !(i1End.compareTo(i2Begin) < 0 || i1Begin.compareTo(i2End) > 0);
	}

	/**
	 * Check if the first interval is laying inside the second
	 *
	 * @param di1 the first interval
	 * @param di2 the second interval
	 * @return <code>true</code> if the first lays in second one, or <code>false</code> otherwise
	 */
	public static boolean isInside(DateInterval<?, ?> di1, DateInterval<?, ?> di2) {
		return di1.getBegin().compareTo(di2.getBegin()) >= 0
			   && di1.getEnd().compareTo(di2.getEnd()) <= 0;
	}

	/**
	 * Find interval that lays in specified <code>dateInterval</code>
	 *
	 * @param intervals	Collection of interval candidates
	 * @param dateInterval Interval to find candidate in
	 * @return DateInterval from collection that lays inside the specified one, or <code>null</code> if not found
	 */
	public static <T extends TemporaryValue<T>, DI extends DateInterval<T, DI>> DI getInterval(Collection<DI> intervals, DI dateInterval) {

		for (DI di : intervals) {
			if (areIntersecting(di, dateInterval)) {
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
	public static boolean isValid(DateInterval<?, ?> di) {
		return di.getBegin().compareTo(di.getEnd()) <= 0;
	}

	/**
	 * Check if date interval includes specified date
	 *
	 * @param date Date
	 * @param di   DateInterval
	 * @return <code>true</code> if date is inside interval, or <code>false</code> otherwise
	 */
	public static boolean includes(Date date, DateInterval<?, ?> di) {
		return di.getBegin().compareTo(date) <= 0 && di.getEnd().compareTo(date) >= 0;
	}

	/**
	 * Check if date interval includes specified date
	 *
	 * @param date  Date
	 * @param begin Interval begin date
	 * @param end   Interval end date
	 * @return <code>true</code> if date is inside interval, or <code>false</code> otherwise
	 */
	public static boolean includes(Date date, Date begin, Date end) {
		return begin.compareTo(date) <= 0 && end.compareTo(date) >= 0;
	}

	/**
	 * Check if time line is consistent: <ol> <li>All time line is covered</li> <li>No gaps in intervals</li> </ol>
	 *
	 * @param tl TimeLine to check
	 * @return <code>true</code> if time line is consistent, or <code>false</code> otherwise
	 */
	public static <T extends TemporaryValue<T>, DI extends DateInterval<T, DI>> boolean isValid(TimeLine<T, DI> tl) {
		Date date = ApplicationConfig.getPastInfinite();
		for (DI di : tl.getIntervals()) {
			if (!isValid(di)) {
				return false;
			}
			if (!date.equals(di.getBegin())) {
				return false;
			}
			date = DateUtil.next(di.getEnd());
		}

		return date.equals(ApplicationConfig.getFutureInfinite());
	}

	/**
	 * Check if date interval covers the hole time line
	 *
	 * @param di Date interval
	 * @return <code>true</code> if interval begin equals {@link org.flexpay.common.util.config.ApplicationConfig#getPastInfinite()}
	 *         and end equals {@link org.flexpay.common.util.config.ApplicationConfig#getFutureInfinite()}, or
	 *         <code>false</code> otherwise
	 */
	public static boolean coversTimeLine(DateInterval<?, ?> di) {
		return di.getBegin().equals(ApplicationConfig.getPastInfinite()) &&
			   di.getEnd().equals(ApplicationConfig.getFutureInfinite());
	}

	/**
	 * Copy date interval
	 *
	 * @param di DateInterval
	 * @return an interval copy
	 */
	private static <T extends TemporaryValue<T>, DI extends DateInterval<T, DI>> DI copy(DI di) {
		return di.copy();
	}

	/**
	 * Add non-empty date interval to the list
	 *
	 * @param dis List of intervals to update
	 * @param di  DateInterval to add
	 */
	private static <T extends TemporaryValue<T>, DI extends DateInterval<T, DI>> void add(List<DI> dis, DI di) {
		if (di != null && isValid(di)) {
			dis.add(di);
		}
	}

	/**
	 * Get intervals intersection
	 *
	 * @param diOld Old interval
	 * @param diNew New interval
	 * @return List of intervals as a result of intersection operation
	 */
	public static <T extends TemporaryValue<T>, DI extends DateInterval<T, DI>> List<DI> intersect(DI diOld, DI diNew) {

		List<DI> dis = new ArrayList<DI>(3);

		if (!areIntersecting(diOld, diNew)) {
			add(dis, copy(diOld));
			return dis;
		}

		DI di;
		// setup lower bound of interval
		// the old starts before the new
		if (diOld.getBegin().before(diNew.getBegin())) {
			di = copy(diOld);
			// new interval begin is inside the old one, add shorter copy of the old interval
			if (diOld.getEnd().after(diNew.getBegin())) {
				// set old interval end is a day before a new starts
				di.setEnd(DateUtil.previous(diNew.getBegin()));
				add(dis, di);
				di = copy(diNew);
			}
		} else {
			di = copy(diNew);
			di.setBegin(diOld.getBegin());
		}
		// the old interval end is after the new one, end current interval and
		// create a short copy of the old interval
		if (diOld.getEnd().after(diNew.getEnd())) {
			di.setEnd(diNew.getEnd());
			add(dis, di);
			di = copy(diOld);
			// old interval part begin is a next day
			di.setBegin(DateUtil.next(diNew.getEnd()));
		}

		// set current interval end date to the old's end
		di.setEnd(diOld.getEnd());
		add(dis, di);
		return dis;
	}

	/**
	 * Join equal intervals in a time line
	 *
	 * @param tl Time line
	 * @return Updated timeline of intervals that contains no two serial equal intervals
	 */
	public static <T extends TemporaryValue<T>, DI extends DateInterval<T, DI>> TimeLine<T, DI> joinEqualIntervals(TimeLine<T, DI> tl) {
		return new TimeLine<T, DI>(join(tl.getIntervals()));
	}

	/**
	 * Join equal intervals in a list
	 *
	 * @param dis List of date intervals
	 * @return Updated list of intervals that contains no two serial equal intervals
	 */
	private static <T extends TemporaryValue<T>, DI extends DateInterval<T, DI>> List<DI> join(List<DI> dis) {
		List<DI> disNew = new ArrayList<DI>(dis.size());
		DI prev = null;
		for (DI di : dis) {
			if (prev == null) {
				prev = di;
				continue;
			}
			if (prev.dataEquals(di)) {
				prev.setEnd(di.getEnd());
			} else {
				add(disNew, prev);
				prev = di;
			}
		}
		add(disNew, prev);
		return disNew;
	}

	/**
	 * Create new TimeLine with new added interval.
	 * <p/>
	 * If begin or end bound of <code>di</code> is inside of some interval in time line the old interval should be
	 * shorthanded
	 *
	 * @param tl TimeLine
	 * @param di DateInterval to add
	 * @return new time line
	 */
	public static <T extends TemporaryValue<T>, DI extends DateInterval<T, DI>> TimeLine<T, DI> addInterval(TimeLine<T, DI> tl, DI di) {
		List<DI> tlNew = new ArrayList<DI>();
		for (DI diOld : tl.getIntervals()) {
			diOld.invalidate();
			tlNew.addAll(intersect(diOld, di));
		}

		if (tlNew.isEmpty()) {
			return new TimeLine<T, DI>(di);
		}

		// join equal data intervals
		return new TimeLine<T, DI>(join(tlNew));
	}

	/**
	 * Create new TimeLine with deleted interval
	 *
	 * @param tl TimeLine
	 * @param di DateInterval to delete
	 * @return new time line
	 */
	public static <T extends TemporaryValue<T>, DI extends DateInterval<T, DI>> TimeLine<T, DI> deleteInterval(TimeLine<T, DI> tl, DI di) {

		List<DI> dis = new ArrayList<DI>();

		// if interval covers the whole time line - return time line with empty data
		if (coversTimeLine(di)) {
			DI nullInterval = di.getEmpty();
			dis.add(nullInterval);
			return new TimeLine<T, DI>(dis);
		}

		DI prev = null;
		boolean found = false;
		for (DI candidate : tl.getIntervals()) {
			if (candidate.equals(di)) {
				// found interval
				if (prev != null) {
					prev.setEnd(candidate.getEnd());
				} else {
					found = true;
				}
			} else {
				prev = copy(candidate);
				// interval already found, and was the first one
				if (found) {
					prev.setBegin(di.getBegin());
					// do not need to set up begin any more
					found = false;
				}
				add(dis, prev);
			}
		}

		return new TimeLine<T, DI>(join(dis));
	}

	/**
	 * Build pair of timelines with all intervals present on both lines
	 *
	 * @param oldTl the first time line
	 * @param newTl the second time line
	 * @return intervals pairs of time line having no intersections
	 */
	public static <T extends TemporaryValue<T>, DI extends DateInterval<T, DI>> Pair<TimeLine<T, DI>, TimeLine<T, DI>> split(@NotNull TimeLine<T, DI> oldTl, @NotNull TimeLine<T, DI> newTl) {
		List<DI> disOld = new ArrayList<DI>();
		List<DI> disNew = new ArrayList<DI>();

		Iterator<DI> itOld = oldTl.getIntervals().iterator();
		Iterator<DI> itNew = newTl.getIntervals().iterator();

		if (!itOld.hasNext() || !itNew.hasNext()) {
			disOld.addAll(oldTl.getIntervals());
			disNew.addAll(newTl.getIntervals());
			return PairUtil.pair(new TimeLine<T, DI>(disOld), new TimeLine<T, DI>(disNew));
		}

		// at the start of the cycle
		DI diOld = itOld.next();
		DI diNew = itNew.next();
		for (DI diOldC = copy(diOld), diNewC = copy(diNew); ;) {
			// old interval end is before the new one's end
			int cmp = diOldC.getEnd().compareTo(diNewC.getEnd());
			if (cmp < 0) {
				DI tmp = copy(diNewC);
				tmp.setEnd(diOldC.getEnd());
				diNewC.setBegin(DateUtil.next(diOldC.getEnd()));
				// add pair of intervals
				add(disOld, diOldC);
				add(disNew, tmp);
				diOldC = copy(itOld.next());
				// new interval end is before the new one's end
			} else if (cmp > 0) {
				DI tmp = copy(diOldC);
				tmp.setEnd(diNewC.getEnd());
				diOldC.setBegin(DateUtil.next(diNewC.getEnd()));
				// add pair of intervals
				add(disOld, tmp);
				add(disNew, diNewC);
				diNewC = copy(itNew.next());
				// intervals ends are the same, just dump to lists
			} else {
				add(disOld, diOldC);
				add(disNew, diNewC);
				// check if there are more intervals (should be enough of
				// just one iterator checking)
				if (itOld.hasNext() && itNew.hasNext()) {
					diOldC = copy(itOld.next());
					diNewC = copy(itNew.next());
				} else {
					break;
				}
			}
		}

		return PairUtil.pair(new TimeLine<T, DI>(disOld), new TimeLine<T, DI>(disNew));
	}

	/**
	 * Build time lines diff list
	 *
	 * @param oldTl the first time line
	 * @param newTl the second time line
	 * @return List if different interval pairs
	 */
	public static <T extends TemporaryValue<T>, DI extends DateInterval<T, DI>> List<Pair<DI, DI>> diff(@NotNull TimeLine<T, DI> oldTl, @NotNull TimeLine<T, DI> newTl) {
		Pair<TimeLine<T, DI>, TimeLine<T, DI>> splittedPair = split(oldTl, newTl);
		List<Pair<DI, DI>> diffs = new ArrayList<Pair<DI, DI>>();
		Iterator<DI> itOld = splittedPair.getFirst().getIntervals().iterator();
		Iterator<DI> itNew = splittedPair.getSecond().getIntervals().iterator();
		for (; itOld.hasNext() && itNew.hasNext();) {
			DI diOld = itOld.next();
			DI diNew = itNew.next();
			if (!diOld.dataEquals(diNew)) {
				diffs.add(PairUtil.pair(diOld, diNew));
			}
		}

		return diffs;
	}

	/**
	 * Invalidate time line, i.e. invalidate every interval in time line
	 *
	 * @param tl TimeLine to invalidate
	 * @return Invalidated time line
	 */
	public static <T extends TemporaryValue<T>, DI extends DateInterval<T, DI>> TimeLine<T, DI> invalidate(TimeLine<T, DI> tl) {
		for (DI di : tl.getIntervals()) {
			di.invalidate();
		}

		return tl;
	}

	/**
	 * Format DateInterval begin and end date values
	 *
	 * @param di DateInterval
	 * @return Array of two strings date representations
	 */
	@NotNull
	public static String[] format(@NotNull DateInterval<?, ?> di) {
		return new String[] {DateUtil.format(di.getBegin()), DateUtil.format(di.getEnd())};
	}
}
