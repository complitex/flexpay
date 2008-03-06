package org.flexpay.eirc.persistence;

import org.flexpay.ab.persistence.District;
import org.flexpay.common.persistence.DomainObjectWithStatus;

public class Organisation extends DomainObjectWithStatus {

	private String individualTaxNumber;
	private String kpp; // code prichiny postanovki na nalogoviy uchet (TODO: translate me)
	private String description;

	private String name;
	private District district;

	/**
	 * Constructs a new DomainObject.
	 */
	public Organisation() {
	}

	public Organisation(Long id) {
		super(id);
	}

	public String getIndividualTaxNumber() {
		return individualTaxNumber;
	}

	public void setIndividualTaxNumber(String individualTaxNumber) {
		this.individualTaxNumber = individualTaxNumber;
	}

	public String getKpp() {
		return kpp;
	}

	public void setKpp(String kpp) {
		this.kpp = kpp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}
}
