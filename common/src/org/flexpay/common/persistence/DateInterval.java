package org.flexpay.common.persistence;

import org.apache.commons.lang.builder.*;
import org.apache.commons.lang.time.DateUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;

import java.util.Calendar;
import java.util.Date;

/**
 * Date interval is a interval of time with day granularity
 *
 * @param <T> Temporary value type
 * @param <DI> DateInterval type
 */
public abstract class DateInterval<T extends TemporaryValue<T>, DI extends DateInterval<T, DI>>
		extends DomainObject implements Comparable<DI> {

	private Date begin = ApplicationConfig.getPastInfinite();
	private Date end = ApplicationConfig.getFutureInfinite();
	private Date invalidDate = ApplicationConfig.getFutureInfinite();
	private Date createDate = DateUtil.now();

	private T value;

	/**
	 * Constructs a new DateInterval.
	 *
	 * @param value Temporary values assigned to this interval
	 */
	public DateInterval(T value) {
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
		doSetBegin(this, begin);
		doSetEnd(this, end);
		this.value = value;

		if (this.begin.compareTo(this.end) > 0) {
			throw new IllegalArgumentException("Dates specified are invalid for interval: [" +
											   this.begin + ", " + this.end + "]");
		}
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		doSetBegin(this, begin);
	}

	private static <T extends TemporaryValue<T>, DI extends DateInterval<T, DI>>
	void doSetBegin(DateInterval<T, DI> di, Date begin) {
		Date pastInfinite = ApplicationConfig.getPastInfinite();
		if (begin == null || begin.compareTo(pastInfinite) < 0) {
			di.begin = pastInfinite;
		} else {
			di.begin = DateUtils.truncate(begin, Calendar.DAY_OF_MONTH);
		}
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		doSetEnd(this, end);
	}


	private static <T extends TemporaryValue<T>, DI extends DateInterval<T, DI>>
	void doSetEnd(DateInterval<T, DI> di, Date end) {
		Date futureInfinite = ApplicationConfig.getFutureInfinite();
		if (end == null || end.compareTo(futureInfinite) > 0) {
			di.end = futureInfinite;
		} else {
			di.end = DateUtils.truncate(end, Calendar.DAY_OF_MONTH);
		}
	}

	public Date getInvalidDate() {
		return invalidDate;
	}

	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void invalidate() {
		this.invalidDate = DateUtil.now();
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

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * Check if data assigned to intervals are equal
	 *
	 * @param di Date interval
	 * @return <code>true</code> if interval datum are equal
	 */
	public boolean dataEquals(DateInterval<T, DI> di) {
		return (value == null && di.getValue() == null) ||
			   (value != null && value.equals(di.getValue()));
	}

    @Override
	public int compareTo(DI o) {

		if (this == o) {
			return 0;
		}

		CompareToBuilder builder = new CompareToBuilder().
				append(begin, o.getBegin()).
				append(end, o.getEnd()).
				append(createDate, o.getCreateDate()).
				append(invalidDate, o.getInvalidDate());

		if (value == null && o.getValue() == null) {
			return builder.toComparison();
		}
		if (value == null) {
			return builder.append(false, true).toComparison();
		}
		if (o.getValue() == null) {
			return builder.append(true, false).toComparison();
		}
		builder.append(value.isEmpty() == o.getValue().isEmpty(), true);

		return builder.toComparison();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("begin", DateUtil.format(begin)).
				append("end", DateUtil.format(end)).
				append("invalidDate", DateUtil.format(invalidDate)).
				append("createDate", DateUtil.format(createDate)).
				append("value", value).
				toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().
				append(begin).
				append(end).
				append(invalidDate).
				append(createDate).
				append(value).
				toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof DateInterval)) {
			return false;
		}

		@SuppressWarnings ({"unchecked"})
		DateInterval<T, DI> that = (DateInterval<T, DI>) obj;
		return new EqualsBuilder().
				append(begin, that.getBegin()).
				append(end, that.getEnd()).
				append(invalidDate, that.getInvalidDate()).
				append(createDate, that.getCreateDate()).
				append(value, that.getValue()).
				isEquals();
	}

}
