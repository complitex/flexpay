package org.flexpay.common.persistence;

import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;

import java.io.Serializable;
import java.util.*;

import static org.flexpay.common.util.CollectionUtils.list;

/**
 * Storage for a sorted list of intervals covering time line
 */
public class TimeLine<T extends TemporaryValue<T>, DI extends DateInterval<T, DI>>
	implements Serializable {

	// a set of intervals is fully covering time line,
	// probably single interval with infinite bounds
	private Collection<DI> intervals;
	private boolean intervalsChecked = false;

	/**
	 * Create time line from single date interval
	 *
	 * @param di Date interval
	 */
	public TimeLine(DI di) {
		intervals = new ArrayList<DI>(3);
		if (!di.getBegin().equals(ApplicationConfig.getPastInfinite())) {
			DI copy = di.getEmpty();
			copy.setBegin(ApplicationConfig.getPastInfinite());
			copy.setEnd(DateUtil.previous(di.getBegin()));
			intervals.add(copy);
		}
		intervals.add(di);

		if (!di.getEnd().equals(ApplicationConfig.getFutureInfinite())) {
			DI copy = di.getEmpty();
			copy.setBegin(DateUtil.next(di.getEnd()));
			copy.setEnd(ApplicationConfig.getFutureInfinite());
			intervals.add(copy);
		}
		intervalsChecked = true;
	}

	/**
	 * Constructor
	 *
	 * @param intervals Date intervals list
	 */
	public TimeLine(Collection<DI> intervals) {
		this.intervals = intervals;
		intervalsChecked = false;
//		if (!DateIntervalUtil.isValid(this)) {
//			throw new IllegalArgumentException("Set of intervals is invalid: " + toString());
//		}
	}

	/**
	 * Getter for property 'intervals'.
	 *
	 * @return Value for property 'intervals'.
	 */
	public List<DI> getIntervals() {
		if (!intervalsChecked) {
			List<DI> dis = list();
			for (DI di : intervals) {
				if (di != null && di.getInvalidDate().equals(ApplicationConfig.getFutureInfinite())) {
					dis.add(di);
				}
			}
			intervals = dis;
			intervalsChecked = true;
		}
		return (List<DI>) intervals;
	}

	/**
	 * Invalid all intervals in a timeline
	 */
	public void invalidate() {
		for (DI di : getIntervals()) {
			di.invalidate();
		}
	}

	/**
	 * Getter for property 'intervals'.
	 *
	 * @return Value for property 'intervals'.
	 */
	public SortedSet<DI> getIntervalsSet() {
		return new TreeSet<DI>(getIntervals());
	}

	/**
	 * Setter for property 'intervals'.
	 *
	 * @param intervals Value to set for property 'intervals'.
	 */
	public void setIntervals(List<DI> intervals) {
		this.intervals = intervals;
	}

	/**
	 * Get values list
	 *
	 * @return values
	 */
	public List<T> getValues() {
		List<T> values = new ArrayList<T>(getIntervals().size());
		for (DI di : getIntervals()) {
			values.add(di.getValue());
		}

		return values;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder(super.toString());
		builder.append(" {\n");
		for (DateInterval<T, DI> di : getIntervals()) {
			String[] dates = DateIntervalUtil.format(di);
			builder.append(dates[0]).append(" : ").append(dates[1])
					.append(", ").append(di.getValue()).append("\n");
		}
		builder.append("}");

		return builder.toString();
	}
}
