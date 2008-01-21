package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.NameDateInterval;

public class StreetNameTemporal extends NameDateInterval<StreetName, StreetNameTemporal> {
	/**
	 * Constructs a new RegionNameTemporal.
	 */
	public StreetNameTemporal() {
		super(new StreetName());
	}

	/**
	 * Copy constructs a new RegionNameTemporal.
	 *
	 * @param di Another name temporal
	 */
	private StreetNameTemporal(NameDateInterval<StreetName, StreetNameTemporal> di) {
		super(di.getBegin(), di.getEnd(), di.getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	protected StreetNameTemporal doGetCopy(NameDateInterval<StreetName, StreetNameTemporal> di) {
		return new StreetNameTemporal(di);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof StreetNameTemporal)) {
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
