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
	protected EircServiceOrganization() {
	}

	public EircServiceOrganization(Long id) {
		super(id);
	}

	public static EircServiceOrganization newInstance() {
		return new EircServiceOrganization();
	}

	public Set<ServedBuilding> getBuildings() {
		return servedBuildings;
	}

	public void setBuildings(Set<ServedBuilding> servedBuildings) {
		this.servedBuildings = servedBuildings;
	}
}
