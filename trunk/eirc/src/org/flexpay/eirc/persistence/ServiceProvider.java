package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObject;

public class ServiceProvider extends DomainObject {

	/**
	 * Constructs a new DomainObject.
	 */
	public ServiceProvider() {
	}

	public ServiceProvider(Long id) {
		super(id);
	}

	private String description;
	private Organisation organisation;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}
}
