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

	public DistrictNameTemporal(District district) {
		this();
		setObject(district);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {

		return obj instanceof DistrictNameTemporal && super.equals(obj);
	}

	protected DistrictNameTemporal doGetCopy(NameDateInterval<DistrictName, DistrictNameTemporal> di) {
		return new DistrictNameTemporal(di);
	}
}
