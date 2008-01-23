package org.flexpay.common.persistence;

import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Storage for a sorted list of intervals covering time line
 */
public class TimeLine<T extends TemporaryValue<T>, DI extends DateInterval<T, DI>> {

	// a set of intervals fully covering time line,
	// probably single interval with infinite bounds
	private List<DI> intervals;
	private transient boolean intervalsChecked = false;

	/**
	 * Create time line from single date interval
	 *
	 * @param di Date interval
	 */
	public TimeLine(DI di) {
		intervals = new ArrayList<DI>(3);
		if (!di.getBegin().equals(ApplicationConfig.getInstance().getPastInfinite())) {
			DI copy = di.getEmpty();
			copy.setBegin(ApplicationConfig.getInstance().getPastInfinite());
			copy.setEnd(DateIntervalUtil.previous(di.getBegin()));
			intervals.add(copy);
		}
		intervals.add(di);
		if (!di.getEnd().equals(ApplicationConfig.getInstance().getFutureInfinite())) {
			DI copy = di.getEmpty();
			copy.setBegin(DateIntervalUtil.next(di.getEnd()));
			copy.setEnd(ApplicationConfig.getInstance().getFutureInfinite());
			intervals.add(copy);
		}
		intervalsChecked = true;
	}

	/**
	 * Constructor
	 *
	 * @param intervals Date intervals list
	 */
	public TimeLine(List<DI> intervals) {
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
			List<DI> dis = new ArrayList<DI>();
			for (DI di : intervals) {
				if (di != null) {
					dis.add(di);
				}
			}
			intervals = dis;
			intervalsChecked = true;
		}
		return intervals;
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
		for (DateInterval di : getIntervals()) {
			String[] dates = DateIntervalUtil.format(di);
			builder.append(dates[0]).append(" : ").append(dates[1]).
					append(", Value: ").append(di == null ? "null" : di.getValue()).append("\n");
		}
		builder.append("}");

		return builder.toString();
	}
}
