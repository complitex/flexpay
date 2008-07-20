package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Date;
import java.util.Calendar;

/**
 * Date interval is a interval of time with day granularity
 */
public abstract class DateInterval<T extends TemporaryValue<T>, DI extends DateInterval<T, DI>>
		extends DomainObject implements Comparable<DI> {

	private Date begin;
	private Date end;
	private Date invalidDate;
	private Date createDate;

	private T value;

	/**
	 * Constructs a new DateInterval.
	 *
	 * @param value Temporary values assigned to this interval
	 */
	public DateInterval(T value) {
		begin = ApplicationConfig.getInstance().getPastInfinite();
		end = ApplicationConfig.getInstance().getFutureInfinite();
		invalidDate = ApplicationConfig.getInstance().getFutureInfinite();
		createDate = DateUtil.now();
		this.value = value;
	}

	/**
	 * Construct a new DateInterval
	 *
	 * @param begin Interval begin date
	 * @param end   Interval end date
	 * @param value Temporary values assigned to this interval
	 */
	public DateInterval(Date begin, Date end, T value) {
		setBegin(begin);
		setEnd(end);
		invalidDate = ApplicationConfig.getInstance().getFutureInfinite();
		createDate = DateUtil.now();
		this.value = value;

		if (!DateIntervalUtil.isValid(this)) {
			throw new IllegalArgumentException("Dates specified are invalid for interval");
		}
	}

	/**
	 * Getter for property 'begin'.
	 *
	 * @return Value for property 'begin'.
	 */
	public Date getBegin() {
		return begin;
	}

	/**
	 * Setter for property 'begin'.
	 *
	 * @param begin Value to set for property 'begin'.
	 */
	public void setBegin(Date begin) {
		Date pastInfinite = ApplicationConfig.getInstance().getPastInfinite();
		if (begin == null || begin.compareTo(pastInfinite) < 0) {
			this.begin = pastInfinite;
		} else {
			this.begin = DateUtils.truncate(begin, Calendar.DAY_OF_MONTH);
		}
	}

	/**
	 * Getter for property 'end'.
	 *
	 * @return Value for property 'end'.
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * Setter for property 'end'.
	 *
	 * @param end Value to set for property 'end'.
	 */
	public void setEnd(Date end) {
		Date futureInfinite = ApplicationConfig.getInstance().getFutureInfinite();
		if (end == null || end.compareTo(futureInfinite) > 0) {
			this.end = futureInfinite;
		} else {
			this.end = DateUtils.truncate(end, Calendar.DAY_OF_MONTH);
		}
	}

	/**
	 * Getter for property 'invalidDate'.
	 *
	 * @return Value for property 'invalidDate'.
	 */
	public Date getInvalidDate() {
		return invalidDate;
	}

	/**
	 * Setter for property 'invalidDate'.
	 *
	 * @param invalidDate Value to set for property 'invalidDate'.
	 */
	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}

	/**
	 * Getter for property 'createDate'.
	 *
	 * @return Value for property 'createDate'.
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * Setter for property 'createDate'.
	 *
	 * @param createDate Value to set for property 'createDate'.
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void invalidate() {
		this.invalidDate = DateUtil.now();
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("Begin", DateUtil.format(begin))
				.append("End", DateUtil.format(end))
				.append("Created", DateUtil.format(createDate))
				.append("Invalid", DateUtil.format(invalidDate))
				.append("Value", value)
				.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(begin)
				.append(end)
				.append(invalidDate)
				.append(createDate)
				.append(value)
				.toHashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof DateInterval)) {
			return false;
		}

		DateInterval that = (DateInterval) obj;
		return new EqualsBuilder()
				.append(begin, that.getBegin())
				.append(end, that.getEnd())
				.append(invalidDate, that.getInvalidDate())
				.append(createDate, that.getCreateDate())
				.append(value, that.getValue())
				.isEquals();
	}

	/**
	 * Create a new copy of this interval.
	 *
	 * @return Date interval copy
	 */
	public abstract DI copy();

	/**
	 * Create a new copy of this interval with no data assigned
	 *
	 * @return Date interval with no data
	 */
	public DI getEmpty() {
		DI copy = copy();
		copy.setValue(value.getEmpty());

		return copy;
	}

	/**
	 * Getter for property 'value'.
	 *
	 * @return Value for property 'value'.
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Setter for property 'value'.
	 *
	 * @param value Value to set for property 'value'.
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * Check if data assigned to intervals are equal
	 *
	 * @param di Date interval
	 * @return <code>true</code> if interval datum are equal
	 */
	public boolean dataEquals(DateInterval di) {
		return (value == null && di.getValue() == null) ||
			   (value != null && value.equals(di.getValue()));
	}

	public int compareTo(DI o) {
		return begin.compareTo(o.getBegin());
	}
}
