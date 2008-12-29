package org.flexpay.sz.persistence.corrections;

import org.flexpay.ab.persistence.District;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.sz.persistence.Oszn;

/**
 * District correction is a mapping from internal District object to third party
 * organizations district codes (internal ids, whatever)
 */
public class DistrictCorrection extends DomainObject {

	// internal district object
	private District district;

	// third party organization id
	private String externalId;

	// third party organization (OSZN for now)
	private Oszn oszn;

	/**
	 * Constructs a new DistrictCorrection.
	 */
	public DistrictCorrection() {
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

	/**
	 * Getter for property 'externalId'.
	 *
	 * @return Value for property 'externalId'.
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Setter for property 'externalId'.
	 *
	 * @param externalId Value to set for property 'externalId'.
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * Getter for property 'oszn'.
	 *
	 * @return Value for property 'oszn'.
	 */
	public Oszn getOszn() {
		return oszn;
	}

	/**
	 * Setter for property 'oszn'.
	 *
	 * @param oszn Value to set for property 'oszn'.
	 */
	public void setOszn(Oszn oszn) {
		this.oszn = oszn;
	}
}
