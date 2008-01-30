package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.Set;

/**
 * Building
 */
public class Building extends DomainObjectWithStatus {

	private District district;
	private Set<BuildingStatus> buildingStatuses = Collections.emptySet();
	private Set<Buildings> buildingses = Collections.emptySet();
	private Set<Apartment> apartments = Collections.emptySet();

	public Building() {
	}

	public District getDistrict() {
		return this.district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Set<BuildingStatus> getBuildingStatuses() {
		return this.buildingStatuses;
	}

	public void setBuildingStatuses(Set<BuildingStatus> buildingStatuses) {
		this.buildingStatuses = buildingStatuses;
	}

	public Set<Buildings> getBuildingses() {
		return this.buildingses;
	}

	public void setBuildingses(Set<Buildings> buildingses) {
		this.buildingses = buildingses;
	}

	public Set<Apartment> getApartments() {
		return this.apartments;
	}

	public void setApartments(Set<Apartment> apartments) {
		this.apartments = apartments;
	}
}
