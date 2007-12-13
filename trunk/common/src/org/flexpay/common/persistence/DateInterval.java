package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.flexpay.common.util.DateIntervalUtil;

import java.util.Date;
import java.io.Serializable;

public class DateInterval implements Serializable {
	private Date begin;
	private Date end;

	/** Constructs a new DateInterval. */
	public DateInterval() {
	}

	/**
	 * Construct a new DateInterval
	 * @param begin Interval begin date
	 * @param end Interval end date
	 */
	public DateInterval(Date begin, Date end) {
		this.begin = begin;
		this.end = end;
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
		this.begin = begin;
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
		this.end = end;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
				.append(DateIntervalUtil.format(this))
				.toString();
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(begin)
				.append(end)
				.toHashCode();
	}

	/** {@inheritDoc} */
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
				.isEquals();
	}
}
