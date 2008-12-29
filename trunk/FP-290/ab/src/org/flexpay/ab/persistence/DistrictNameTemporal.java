package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.NameDateInterval;

public class DistrictNameTemporal extends NameDateInterval<DistrictName, DistrictNameTemporal> {

	/**
	 * Constructs a new RegionNameTemporal.
	 */
	public DistrictNameTemporal() {
		super(new DistrictName());
	}

	/**
	 * Copy constructs a new RegionNameTemporal.
	 *
	 * @param di Another name temporal
	 */
	private DistrictNameTemporal(NameDateInterval<DistrictName, DistrictNameTemporal> di) {
		super(di.getBegin(), di.getEnd(), di.getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof DistrictNameTemporal)) {
			return false;
		}
		return super.equals(obj);
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

	protected DistrictNameTemporal doGetCopy(NameDateInterval<DistrictName, DistrictNameTemporal> di) {
		return new DistrictNameTemporal(di);
	}
}
