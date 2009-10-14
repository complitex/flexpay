package org.flexpay.sz.persistence.corrections;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.Street;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.sz.persistence.Oszn;

/**
 * Street correction is a mapping from internal Street object to third party organizations
 * Street codes (internal ids, whatever)
 */
public class StreetCorrection extends DomainObject {

	// internal street object
	private Street street;

	// third party organization id
	private String externalId;

	// third party organization (OSZN for now)
	private Oszn oszn;

	public Street getStreet() {
		return street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public Oszn getOszn() {
		return oszn;
	}

	public void setOszn(Oszn oszn) {
		this.oszn = oszn;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("externalId", externalId).
				toString();
	}

}
