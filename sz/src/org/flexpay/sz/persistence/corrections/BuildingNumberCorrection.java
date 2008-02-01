package org.flexpay.sz.persistence.corrections;

import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.sz.persistence.Oszn;

/**
 * Building correction is a mapping from internal Building object to third party
 * organisations Building numbers (internal ids, whatever)
 */
public class BuildingNumberCorrection extends DomainObject {

	// internal building object
	private Building building;

	// external building number
	private String externalNumber;

	// optional external building bulk
	private String externalBulk;

	// street correction
	private StreetCorrection streetCorrection;

	// third party organisation (OSZN for now)
	private Oszn oszn;

	/**
	 * Constructs a new BuildingNumberCorrection.
	 */
	public BuildingNumberCorrection() {
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

	/**
	 * Getter for property 'streetCorrection'.
	 *
	 * @return Value for property 'streetCorrection'.
	 */
	public StreetCorrection getStreetCorrection() {
		return streetCorrection;
	}

	/**
	 * Setter for property 'streetCorrection'.
	 *
	 * @param streetCorrection Value to set for property 'streetCorrection'.
	 */
	public void setStreetCorrection(StreetCorrection streetCorrection) {
		this.streetCorrection = streetCorrection;
	}

	/**
	 * Getter for property 'externalBulk'.
	 *
	 * @return Value for property 'externalBulk'.
	 */
	public String getExternalBulk() {
		return externalBulk;
	}

	/**
	 * Setter for property 'externalBulk'.
	 *
	 * @param externalBulk Value to set for property 'externalBulk'.
	 */
	public void setExternalBulk(String externalBulk) {
		this.externalBulk = externalBulk;
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
	 * Getter for property 'building'.
	 *
	 * @return Value for property 'building'.
	 */
	public Building getBuilding() {
		return building;
	}

	/**
	 * Setter for property 'building'.
	 *
	 * @param building Value to set for property 'building'.
	 */
	public void setBuilding(Building building) {
		this.building = building;
	}
}
