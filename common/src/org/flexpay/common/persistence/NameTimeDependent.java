package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.flexpay.common.util.DateIntervalUtil;

import java.util.Date;
import java.util.List;

/**
 * Base class for objects that has time-dependent name
 */
public class NameTimeDependent<T extends TemporaryValue<T>, DI extends DateInterval<T, DI>>
		extends DomainObjectWithStatus {

	private TimeLine<T, DI> namesTimeLine;

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
	}

	/**
	 * Setter for property 'nameTemporals'.
	 *
	 * @param nameTemporals Value to set for property 'nameTemporals'.
	 */
	public void setNameTemporals(List<DI> nameTemporals) {
		namesTimeLine = new TimeLine<T, DI>(nameTemporals);
	}

	/**
	 * Find value for date
	 *
	 * @param dt Date to get value for
	 * @return Value which interval includes specified date, or <code>null</code> if not
	 *         found
	 */
	public T getNameForDate(Date dt) {
		List<DI> intervals = namesTimeLine.getIntervals();
		for (DI di : intervals) {
			if (DateIntervalUtil.includes(dt, di)) {
				return di.getValue();
			}
		}

		return null;
	}

	/**
	 * Find value for current date
	 *
	 * @return Value which interval includes specified date, or <code>null</code> if not
	 *         found
	 */
	public T getCurrentName() {
		return getNameForDate(DateIntervalUtil.now());
	}

	/**
	 * Getter for property 'nameTemporals'.
	 *
	 * @return Value for property 'nameTemporals'.
	 */
	public List<DI> getNameTemporals() {
		return namesTimeLine.getIntervals();
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

		NameTimeDependent that = (NameTimeDependent) obj;

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
