package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Base class for objects that has time-dependent name
 */
public class NameTimeDependent<T extends TemporaryValue<T>, DI extends DateInterval<T, DI>>
		extends DomainObjectWithStatus {

	private static final SortedSet EMPTY_SORTED_SET =
			Collections.unmodifiableSortedSet(new TreeSet());

	private SortedSet<DI> nameTemporals = EMPTY_SORTED_SET;
	private TimeLine<T, DI> namesTimeLine;

	public NameTimeDependent() {
	}

	public NameTimeDependent(Long id) {
		super(id);
	}

	/**
	 * Getter for property 'namesTimeLine'.
	 *
	 * @return Value for property 'namesTimeLine'.
	 */
	public TimeLine<T, DI> getNamesTimeLine() {
		return namesTimeLine;
	}

	/**
	 * Setter for property 'namesTimeLine'.
	 *
	 * @param namesTimeLine Value to set for property 'namesTimeLine'.
	 */
	public void setNamesTimeLine(TimeLine<T, DI> namesTimeLine) {
		this.namesTimeLine = namesTimeLine;

		if (nameTemporals == EMPTY_SORTED_SET) {
			nameTemporals = CollectionUtils.treeSet();
		}
		nameTemporals.addAll(namesTimeLine.getIntervals());
	}

	/**
	 * Setter for property 'nameTemporals'.
	 *
	 * @param nameTemporals Value to set for property 'nameTemporals'.
	 */
	public void setNameTemporals(SortedSet<DI> nameTemporals) {
		this.nameTemporals = nameTemporals;
		namesTimeLine = new TimeLine<T, DI>(nameTemporals);
	}

	protected void addNameTemporal(DI temporal) {
		TimeLine<T, DI> tlNew;
		if (namesTimeLine == null) {
			tlNew = new TimeLine<T, DI>(temporal);
		} else {
			tlNew = DateIntervalUtil.addInterval(namesTimeLine, temporal);
		}

		if (namesTimeLine == tlNew) {
			// nothing to do, timeline did not changed
			return;
		}

		if (nameTemporals == EMPTY_SORTED_SET) {
			nameTemporals = CollectionUtils.treeSet();
		}

		// add all new intervals
		nameTemporals.addAll(tlNew.getIntervals());

		// invalidate old timeline and assign to a new
//		if (namesTimeLine != null) {
//			namesTimeLine.invalidate();
//		}
		namesTimeLine = tlNew;
	}

	/**
	 * Getter for property 'nameTemporals'.
	 *
	 * @return Value for property 'nameTemporals'.
	 */
	@NotNull
	public SortedSet<DI> getNameTemporals() {

//		for (DI temporal : nameTemporals) {
//			if (temporal.getValue() != null && temporal.getValue().isNew()) {
//				temporal.setValue(null);
//			}
//		}

		return nameTemporals;
	}

	/**
	 * Find temporal for current date
	 *
	 * @return Temporal which interval includes specified date, or <code>null</code> if not
	 *         found
	 */
	@Nullable
	public DI getCurrentNameTemporal() {
		return getNameTemporalForDate(DateUtil.now());
	}

	/**
	 * Find value for date
	 *
	 * @param dt Date to get value for
	 * @return Temporal which interval includes specified date, or <code>null</code> if not
	 *         found
	 */
	@Nullable
	public DI getNameTemporalForDate(Date dt) {

		if (namesTimeLine == null) {
			return null;
		}

		List<DI> intervals = namesTimeLine.getIntervals();
		for (DI di : intervals) {
			if (DateIntervalUtil.includes(dt, di)) {
				return di;
			}
		}

		return null;
	}

	/**
	 * Find value for date
	 *
	 * @param dt Date to get value for
	 * @return Value which interval includes specified date, or <code>null</code> if not
	 *         found
	 */
	@Nullable
	public T getNameForDate(Date dt) {
		DI temporal = getNameTemporalForDate(dt);
		return temporal != null ? temporal.getValue() : null;
	}

	/**
	 * Find value for current date
	 *
	 * @return Value which interval includes specified date, or <code>null</code> if not
	 *         found
	 */
	@Nullable
	public T getCurrentName() {
		DI temporal = getCurrentNameTemporal();
		return temporal != null ? temporal.getValue() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof NameTimeDependent)) {
			return false;
		}

		NameTimeDependent<T, DI> that = (NameTimeDependent<T, DI>) obj;

		return new EqualsBuilder()
				.append(namesTimeLine, that.namesTimeLine)
				.isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(namesTimeLine)
				.toHashCode();
	}
}
