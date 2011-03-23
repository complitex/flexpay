package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import static java.util.Collections.max;
import static java.util.Collections.min;
import static org.flexpay.common.util.CollectionUtils.list;

/**
 * Abstract implementation of algorithm building diff for to temporal objects timelines
 */
public class TemporalObjectsHistoryBuildHelper {

	/**
	 * Build diff for two timelines
	 *
	 * @param extractor Data extractor
	 * @param ts1	   the first timeline, ordered by beginDate
	 * @param ts2	   the second timeline, ordered by beginDate
	 * @param diff	  Diff to store changes in
	 * @param <T>       Temporal objects type
	 */
	public static <T extends DomainObject> void buildDiff(
			TemporalDataExtractor<T> extractor, Collection<T> ts1, Collection<T> ts2, Diff diff) {

		Iterator<T> it1 = ts1.iterator();
		Iterator<T> it2 = ts2.iterator();

		Date cursor = ApplicationConfig.getPastInfinite();
		T n1 = null;
		T n2 = null;

		while (it1.hasNext() || it2.hasNext()) {
			n1 = n1 == null && it1.hasNext() ? it1.next() : n1;
			n2 = n2 == null && it2.hasNext() ? it2.next() : n2;

			// setup next intervals boundaries
			@NotNull Date begin1 = n1 != null ? extractor.getBeginDate(n1) : ApplicationConfig.getFutureInfinite();
			@NotNull Date begin2 = n2 != null ? extractor.getBeginDate(n2) : ApplicationConfig.getFutureInfinite();
			@NotNull Date end1 = n1 != null ? extractor.getEndDate(n1) : ApplicationConfig.getFutureInfinite();
			@NotNull Date end2 = n2 != null ? extractor.getEndDate(n2) : ApplicationConfig.getFutureInfinite();

			// setup lower and upper bound for a next pair of intervals to build diffs on
			Date beginMin = min(list(begin1, begin2));
			Date beginMax = max(list(begin1, begin2));
			Date end = min(list(end1, end2));
			Date beginLower = min(list(beginMax, end));

			// add diff in interval from cursor to min among begins
			preBuildDiff(extractor, cursor, beginMin, n1, n2, diff);

			// set cursor to next point - first begin, or cursor if it was after begin
			cursor = max(list(beginMin, cursor));
			// now add diff from cursor to lower value of bigger begin and two ends
			preBuildDiff(extractor, cursor, beginLower, n1, n2, diff);

			cursor = beginLower;
			// if not reached any of ends, add diff from max begin to min end
			if (cursor.before(end)) {
				preBuildDiff(extractor, cursor, end, n1, n2, diff);
				cursor = end;
			}

			// if the first end was reached - fetch next value
			if (cursor.compareTo(end1) >= 0) {
				n1 = null;
			}
			// if the second end was reached - fetch next value
			if (cursor.compareTo(end2) >= 0) {
				n2 = null;
			}
		}
	}

	private static <T extends DomainObject> void preBuildDiff(
			TemporalDataExtractor<T> extractor, Date begin, Date end, T t1, T t2, Diff diff) {

		if (begin.compareTo(ApplicationConfig.getFutureInfinite()) >= 0) {
			return;
		}
		if (end.compareTo(ApplicationConfig.getPastInfinite()) <= 0) {
			return;
		}
		if (t1 != null && extractor.getBeginDate(t1).compareTo(end) >= 0) {
			t1 = null;
		}
		if (t2 != null && extractor.getBeginDate(t2).compareTo(end) >= 0) {
			t2 = null;
		}
		if (begin.after(end) || (t1 == null && t2 == null)) {
			return;
		}

		extractor.buildDiff(begin, end, t1, t2, diff);
	}

	/**
	 * Extractor of necessary data of algorithm
	 *
	 * @param <T> Temporal objects type
	 */
	public static interface TemporalDataExtractor<T extends DomainObject> {

		/**
		 * Extract begin date of an object
		 *
		 * @param obj Temporal object
		 * @return Temporal begin date
		 */
		Date getBeginDate(T obj);

		/**
		 * Extract end date of an object
		 *
		 * @param obj Temporal object
		 * @return Temporal end date
		 */
		Date getEndDate(T obj);

		/**
		 * Callback method used to generate actual changes
		 *
		 * @param begin Interval begin date
		 * @param end   Interval end date
		 * @param t1	first temporal
		 * @param t2	second temporal
		 * @param diff  Diff to store changes in
		 */
		void buildDiff(Date begin, Date end, T t1, T t2, Diff diff);
	}
}
