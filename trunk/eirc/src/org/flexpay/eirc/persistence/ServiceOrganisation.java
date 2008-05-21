package org.flexpay.eirc.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.ab.persistence.District;

import java.util.Collections;
import java.util.Set;

/**
 * Service organisation
 */
public class ServiceOrganisation extends DomainObjectWithStatus {

	private Organisation organisation;
	private District district;
	private Set<ServedBuilding> servedBuildings = Collections.emptySet();
	private Set<ServiceOrganisationDescription> descriptions = Collections.emptySet();

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

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Set<ServiceOrganisationDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(Set<ServiceOrganisationDescription> descriptions) {
		this.descriptions = descriptions;
	}
}
