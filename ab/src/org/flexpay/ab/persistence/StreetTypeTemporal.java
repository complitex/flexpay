package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.TypeDateInterval;

public class StreetTypeTemporal extends TypeDateInterval<StreetType, StreetTypeTemporal> {

	/**
	 * Constructs a new TypeTemporal.
	 */
	public StreetTypeTemporal() {
		super(new StreetType());
	}

	/**
	 * Copy constructs a new RegionNameTemporal.
	 *
	 * @param di Another name temporal
	 */
	private StreetTypeTemporal(TypeDateInterval<StreetType, StreetTypeTemporal> di) {
		super(di.getBegin(), di.getEnd(), di.getValue());
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
	protected StreetTypeTemporal doGetCopy(TypeDateInterval<StreetType, StreetTypeTemporal> di) {
		return new StreetTypeTemporal(di);
	}

	public Street getStreet() {
		return (Street) getObject();
	}

	public void setStreet(Street street) {
		setObject(street);
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
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return super.toString();
	}

}
