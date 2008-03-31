package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Set;

/**
 * Service organisation
 */
public class ServiceOrganisation extends DomainObjectWithStatus {

	private Organisation organisation;
	private Set<ServedBuilding> servedBuildings;

	/**
	 * Constructs a new DomainObject.
	 */
	public ServiceOrganisation() {
	}

	public ServiceOrganisation(Long id) {
		super(id);
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public Set<ServedBuilding> getBuildings() {
		return servedBuildings;
	}

	public void setBuildings(Set<ServedBuilding> servedBuildings) {
		this.servedBuildings = servedBuildings;
	}
}
