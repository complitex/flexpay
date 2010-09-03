package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.NameDateInterval;

public class StreetNameTemporal extends NameDateInterval<StreetName, StreetNameTemporal> {

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

	public StreetNameTemporal(Street street) {
		this();
		setObject(street);
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
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
}
