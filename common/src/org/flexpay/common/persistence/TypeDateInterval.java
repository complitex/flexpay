package org.flexpay.common.persistence;

import java.util.Date;

public abstract class TypeDateInterval<T extends TemporaryValue<T>, DI extends TypeDateInterval<T, DI>>
		extends DateInterval<T, DI> {

	private DomainObject object;

	/**
	 * Constructs a new DateInterval.
	 *
	 * @param value Temporary values assigned to this interval
	 */
	protected TypeDateInterval(T value) {
		super(value);
	}

	/**
	 * Construct a new DateInterval
	 *
	 * @param begin Interval begin date
	 * @param end   Interval end date
	 * @param value Temporary values assigned to this interval
	 */
	protected TypeDateInterval(Date begin, Date end, T value) {
		super(begin, end, value);
	}

	/**
	 * Get object that name specifies value.
	 *
	 * @return Value for property 'object'.
	 */
	public DomainObject getObject() {
		return object;
	}

	/**
	 * Setter for property 'object'.
	 *
	 * @param object Value to set for property 'object'.
	 */
	public void setObject(DomainObject object) {
		this.object = object;
	}

	/**
	 * Create a new copy of this interval.
	 *
	 * @return Date interval copy
	 */
	@SuppressWarnings ({"unchecked"})
	public DI copy() {
		TypeDateInterval<T, DI> temporal = doGetCopy(this);
		temporal.setObject(getObject());

		return (DI)temporal;
	}

	/**
	 * Create a copy of interval
	 *
	 * @param di Name date interval
	 * @return a copy
	 */
	protected abstract TypeDateInterval<T, DI> doGetCopy(TypeDateInterval<T, DI> di);
}
