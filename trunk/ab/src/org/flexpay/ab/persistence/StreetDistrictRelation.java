package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObject;

public class StreetDistrictRelation extends DomainObject {

	private Street street;
	private District district;

	public StreetDistrictRelation() {
	}

	public StreetDistrictRelation(Street street, District district) {
		this.street = street;
		this.district = district;
	}

	public Street getStreet() {
		return street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

}
