package org.flexpay.sz.persistence.corrections;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
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

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
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
