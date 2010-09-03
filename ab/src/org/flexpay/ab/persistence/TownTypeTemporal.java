package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.TypeDateInterval;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;

import java.util.Date;

/**
 * Temporal value of town type
 */
public class TownTypeTemporal extends TypeDateInterval<TownType, TownTypeTemporal> {

	/**
	 * Constructs a new TownTypeTemporal.
	 */
	public TownTypeTemporal() {
		super(new TownType());
	}

	/**
	 * Copy constructs a new TownTypeTemporal.
	 *
	 * @param di Another name temporal
	 */
	private TownTypeTemporal(TypeDateInterval<TownType, TownTypeTemporal> di) {
		super(di.getBegin(), di.getEnd(), di.getValue());
	}

	public TownTypeTemporal(Date beginDate, TownType townType) {
		super(beginDate, getFutureInfinite(), townType);
	}

	public TownTypeTemporal(Date beginDate, Stub<TownType> typeStub) {
		this(beginDate, new TownType(typeStub.getId()));
	}

	/**
	 * Create a copy of interval
	 *
	 * @param di Name date interval
	 * @return a copy
	 */
    @Override
	protected TownTypeTemporal doGetCopy(TypeDateInterval<TownType, TownTypeTemporal> di) {
		return new TownTypeTemporal(di);
	}

	/**
	 * Getter for property 'town'.
	 *
	 * @return Value for property 'town'.
	 */
	public Town getTown() {
		return (Town) getObject();
	}

	/**
	 * Setter for property 'town'.
	 *
	 * @param town Value to set for property 'town'.
	 */
	public void setTown(Town town) {
		setObject(town);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof TownTypeTemporal)) {
			return false;
		}
		return super.equals(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return super.toString();
	}
}
