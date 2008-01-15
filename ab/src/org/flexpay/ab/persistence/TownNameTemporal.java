package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.NameDateInterval;

public class TownNameTemporal extends NameDateInterval<TownName, TownNameTemporal> {

	/**
	 * Constructs a new RegionNameTemporal.
	 */
	public TownNameTemporal() {
		super(new TownName());
	}

	/**
	 * Copy constructs a new RegionNameTemporal.
	 *
	 * @param di Another name temporal
	 */
	private TownNameTemporal(NameDateInterval<TownName, TownNameTemporal> di) {
		super(di.getBegin(), di.getEnd(), di.getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	protected TownNameTemporal doGetCopy(NameDateInterval<TownName, TownNameTemporal> di) {
		return new TownNameTemporal(di);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof TownNameTemporal)) {
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
