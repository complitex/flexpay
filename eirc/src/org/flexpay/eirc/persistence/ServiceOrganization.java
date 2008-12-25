package org.flexpay.eirc.persistence;

import java.util.Collections;
import java.util.Set;

/**
 * Service organization
 */
public class ServiceOrganization extends OrganizationInstance<ServiceOrganizationDescription, ServiceOrganization> {

	private Set<ServedBuilding> servedBuildings = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public ServiceOrganization() {
	}

	public ServiceOrganization(Long id) {
		super(id);
	}

	public Set<ServedBuilding> getBuildings() {
		return servedBuildings;
	}

	public void setBuildings(Set<ServedBuilding> servedBuildings) {
		this.servedBuildings = servedBuildings;
	}
}
