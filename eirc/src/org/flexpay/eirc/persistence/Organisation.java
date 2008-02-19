package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;

public class Organisation extends DomainObject {

	private String inn;
	private String kpp;
	private String description;

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
}
