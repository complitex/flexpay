package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DateInterval;

/**
 * Temporal value of town type
 */
public class TownTypeTemporal extends DateInterval<TownType, TownTypeTemporal> {

	private Town town;

	/**
	 * Constructs a new TownTypeTemporal.
	 */
	public TownTypeTemporal() {
		super(new TownType());
	}

	/**
	 * Copy constructs a new TownTypeTemporal.
	 *
	 * @param temporal Another name temporal
	 */
	private TownTypeTemporal(TownTypeTemporal temporal) {
		super(temporal.getBegin(), temporal.getEnd(), temporal.getValue());
	}

	/**
	 * Getter for property 'town'.
	 *
	 * @return Value for property 'town'.
	 */
	public Town getTown() {
		return town;
	}

	/**
	 * Setter for property 'town'.
	 *
	 * @param town Value to set for property 'town'.
	 */
	public void setTown(Town town) {
		this.town = town;
	}

	/**
	 * Create a new copy of this interval.
	 *
	 * @return Date interval copy
	 */
	public TownTypeTemporal copy() {
		TownTypeTemporal temporal = new TownTypeTemporal(this);
		temporal.setTown(getTown());

		return temporal;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (! (obj instanceof TownTypeTemporal)) {
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
