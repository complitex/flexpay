package org.flexpay.sz.persistence.corrections;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.sz.persistence.Oszn;

/**
 * Apartment number correction is a mapping from internal apartment object to third party
 * organizations apartment numbers (internal ids, whatever)
 */
public class ApartmentNumberCorrection extends DomainObject {

	// internal apartment object
	private Apartment apartment;

	// external apartment number
	private String externalNumber;

	// third party organization (OSZN for now)
	private Oszn oszn;

	public Apartment getApartment() {
		return apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public String getExternalNumber() {
		return externalNumber;
	}

	public void setExternalNumber(String externalNumber) {
		this.externalNumber = externalNumber;
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
				append("externalNumber", externalNumber).
				toString();
	}
}
