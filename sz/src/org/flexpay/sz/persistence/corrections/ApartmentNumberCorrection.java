package org.flexpay.sz.persistence.corrections;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.sz.persistence.Oszn;

/**
 * Apartment number correction is a mapping from internal apartment object to third party
 * organisations apartment numbers (internal ids, whatever)
 */
public class ApartmentNumberCorrection extends DomainObject {

	// internal apartment object
	private Apartment apartment;

	// external apartment number
	private String externalNumber;

	// third party organisation (OSZN for now)
	private Oszn oszn;

	/**
	 * Constructs a new ApartmentNumberCorrection.
	 */
	public ApartmentNumberCorrection() {
	}

	/**
	 * Getter for property 'apartment'.
	 *
	 * @return Value for property 'apartment'.
	 */
	public Apartment getApartment() {
		return apartment;
	}

	/**
	 * Setter for property 'apartment'.
	 *
	 * @param apartment Value to set for property 'apartment'.
	 */
	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	/**
	 * Getter for property 'externalNumber'.
	 *
	 * @return Value for property 'externalNumber'.
	 */
	public String getExternalNumber() {
		return externalNumber;
	}

	/**
	 * Setter for property 'externalNumber'.
	 *
	 * @param externalNumber Value to set for property 'externalNumber'.
	 */
	public void setExternalNumber(String externalNumber) {
		this.externalNumber = externalNumber;
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
