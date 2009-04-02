package org.flexpay.eirc.persistence;

import org.flexpay.orgs.persistence.ServiceOrganization;

import java.util.Collections;
import java.util.Set;

/**
 * Service organization
 */
public class EircServiceOrganization extends ServiceOrganization {

	private Set<ServedBuilding> servedBuildings = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public EircServiceOrganization() {
	}

	public EircServiceOrganization(Long id) {
		super(id);
	}

	public Set<ServedBuilding> getBuildings() {
		return servedBuildings;
	}

	public void setBuildings(Set<ServedBuilding> servedBuildings) {
		this.servedBuildings = servedBuildings;
	}
}
