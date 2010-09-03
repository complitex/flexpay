package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.NameDateInterval;

public class RegionNameTemporal extends NameDateInterval<RegionName, RegionNameTemporal> {

	/**
	 * Constructs a new RegionNameTemporal.
	 */
	public RegionNameTemporal() {
		super(new RegionName());
	}



	/**
	 * Copy constructs a new RegionNameTemporal.
	 *
	 * @param di Another name temporal
	 */
	private RegionNameTemporal(NameDateInterval<RegionName, RegionNameTemporal> di) {
		super(di.getBegin(), di.getEnd(), di.getValue());
	}

	public RegionNameTemporal(Region region) {
		this();
		setObject(region);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof RegionNameTemporal)) {
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

    @Override
	protected RegionNameTemporal doGetCopy(NameDateInterval<RegionName, RegionNameTemporal> di) {
		return new RegionNameTemporal(di);
	}
}
