package org.flexpay.eirc.persistence;

import org.flexpay.ab.persistence.District;
import org.flexpay.common.persistence.DomainObjectWithStatus;

public class Organisation extends DomainObjectWithStatus {

	private String inn;
	private String kpp;
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

	public String getInn() {
		return inn;
	}

	public void setInn(String inn) {
		this.inn = inn;
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
