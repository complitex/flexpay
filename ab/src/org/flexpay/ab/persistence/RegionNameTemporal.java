package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DateInterval;

public class RegionNameTemporal extends DateInterval<RegionName, RegionNameTemporal> {
	private Long id;
	private Region region;

	/**
	 * Constructs a new RegionNameTemporal.
	 */
	public RegionNameTemporal() {
		super(new RegionName());
	}

	/**
	 * Copy constructs a new RegionNameTemporal.
	 *
	 * @param nameTemporal Another name temporal
	 */
	private RegionNameTemporal(RegionNameTemporal nameTemporal) {
		super(nameTemporal.getBegin(), nameTemporal.getEnd(), nameTemporal.getValue());
	}

	/**
	 * Getter for property 'id'.
	 *
	 * @return Value for property 'id'.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter for property 'id'.
	 *
	 * @param id Value to set for property 'id'.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter for property 'region'.
	 *
	 * @return Value for property 'region'.
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * Setter for property 'region'.
	 *
	 * @param region Value to set for property 'region'.
	 */
	public void setRegion(Region region) {
		this.region = region;
	}

	/**
	 * Create a new copy of this interval.
	 *
	 * @return Date interval copy
	 */
	public RegionNameTemporal copy() {
		RegionNameTemporal nameTemporal = new RegionNameTemporal(this);
		nameTemporal.setRegion(getRegion());

		return nameTemporal;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (! (obj instanceof RegionNameTemporal)) {
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
