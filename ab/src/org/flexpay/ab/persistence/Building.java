package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

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

	public Building(Long id) {
		super(id);
	}

	@Nullable
	public District getDistrict() {
		return this.district;
	}

	public void setDistrict(@Nullable District district) {
		this.district = district;
	}

	@NotNull
	public Set<BuildingStatus> getBuildingStatuses() {
		return this.buildingStatuses;
	}

	public void setBuildingStatuses(@NotNull Set<BuildingStatus> buildingStatuses) {
		this.buildingStatuses = buildingStatuses;
	}

	@NotNull
	public Set<Buildings> getBuildingses() {
		return this.buildingses;
	}

	public void setBuildingses(@NotNull Set<Buildings> buildingses) {
		this.buildingses = buildingses;
	}

	@NotNull
	public Set<Apartment> getApartments() {
		return this.apartments;
	}

	public void setApartments(@NotNull Set<Apartment> apartments) {
		this.apartments = apartments;
	}

	public void addBuildings(@NotNull Buildings buildings) {
		if (buildingses == Collections.EMPTY_SET) {
			buildingses = new HashSet<Buildings>();
		}

		buildings.setBuilding(this);
		buildingses.add(buildings);
	}

	@Nullable
	public Buildings getDefaultBuildings() {
		for(Buildings buildings : buildingses) {
			if(buildings.getPrimaryStatus() != null && buildings.getPrimaryStatus()) {
				return buildings;
			}
		}
		
		return buildingses.isEmpty() ? null : buildingses.iterator().next();
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.toString();
	}
}
