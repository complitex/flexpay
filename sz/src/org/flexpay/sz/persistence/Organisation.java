package org.flexpay.sz.persistence;

import org.flexpay.ab.persistence.District;
import org.flexpay.common.persistence.DomainObjectWithStatus;

public class Organisation extends DomainObjectWithStatus {

	private String name;
	private District district;

	/**
	 * Constructs a new Organisation.
	 */
	public Organisation() {
	}

	/**
	 * Getter for property 'name'.
	 *
	 * @return Value for property 'name'.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for property 'name'.
	 *
	 * @param name Value to set for property 'name'.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for property 'district'.
	 *
	 * @return Value for property 'district'.
	 */
	public District getDistrict() {
		return district;
	}

	/**
	 * Setter for property 'district'.
	 *
	 * @param district Value to set for property 'district'.
	 */
	public void setDistrict(District district) {
		this.district = district;
	}
}
