package org.flexpay.sz.persistence.corrections;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.sz.persistence.Oszn;

/**
 * Building correction is a mapping from internal Building object to third party
 * organizations Building numbers (internal ids, whatever)
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

	// third party organization (OSZN for now)
	private Oszn oszn;

	public Oszn getOszn() {
		return oszn;
	}

	public void setOszn(Oszn oszn) {
		this.oszn = oszn;
	}

	public StreetCorrection getStreetCorrection() {
		return streetCorrection;
	}

	public void setStreetCorrection(StreetCorrection streetCorrection) {
		this.streetCorrection = streetCorrection;
	}

	public String getExternalBulk() {
		return externalBulk;
	}

	public void setExternalBulk(String externalBulk) {
		this.externalBulk = externalBulk;
	}

	public String getExternalNumber() {
		return externalNumber;
	}

	public void setExternalNumber(String externalNumber) {
		this.externalNumber = externalNumber;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("externalNumber", externalNumber).
				append("externalBulk", externalBulk).
				toString();
	}

}
