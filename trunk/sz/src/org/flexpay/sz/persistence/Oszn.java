package org.flexpay.sz.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.persistence.District;
import org.flexpay.common.persistence.DomainObject;

public class Oszn extends DomainObject {

	private String description;
	//private Organization organization;
	private District district;
	
	/**
	 * Constructs a new Oszn.
	 */
	public Oszn() {
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", getId())
				.append("Description", getDescription())
				.append("District", getDistrict())
				.toString();
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public District getDistrict() {
		return district;
	}
	
	public void setDistrict(District district) {
		this.district = district;
	}
}
