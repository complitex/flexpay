package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.TypeDateInterval;

public class StreetTypeTemporal extends TypeDateInterval<StreetType, StreetTypeTemporal> {

	private Street object;

	/**
	 * Constructs a new TypeTemporal.
	 */
	public StreetTypeTemporal() {
		super(new StreetType());
	}

	/**
	 * Getter for property 'object'.
	 *
	 * @return Value for property 'object'.
	 */
	public Street getObject() {
		return object;
	}

	/**
	 * Setter for property 'object'.
	 *
	 * @param object Value to set for property 'object'.
	 */
	public void setObject(Street object) {
		this.object = object;
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
	protected StreetTypeTemporal doGetCopy(TypeDateInterval<StreetType, StreetTypeTemporal> di) {
		return new StreetTypeTemporal(di);
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
