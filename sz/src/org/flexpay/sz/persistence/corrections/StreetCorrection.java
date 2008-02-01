package org.flexpay.sz.persistence.corrections;

import org.flexpay.ab.persistence.Street;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.sz.persistence.Oszn;

/**
 * Street correction is a mapping from internal Street object to third party organisations
 * Street codes (internal ids, whatever)
 */
public class StreetCorrection extends DomainObject {

	// internal street object
	private Street street;

	// third party organisation id
	private String externalId;

	// third party organisation (OSZN for now)
	private Oszn oszn;

	/**
	 * Constructs a new StreetCorrection.
	 */
	public StreetCorrection() {
	}

	/**
	 * Getter for property 'street'.
	 *
	 * @return Value for property 'street'.
	 */
	public Street getStreet() {
		return street;
	}

	/**
	 * Setter for property 'street'.
	 *
	 * @param street Value to set for property 'street'.
	 */
	public void setStreet(Street street) {
		this.street = street;
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
