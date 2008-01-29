package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.List;

/**
 * Buildings is a logical relation between building and several street addresses
 */
public class Buildings extends DomainObjectWithStatus {

	private Street street;
	private Building building;
	private List<BuildingAttribute> buildingAttributes = Collections.emptyList();

	public Buildings() {
	}

	public Street getStreet() {
		return this.street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

	public Building getBuilding() {
		return this.building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public List<BuildingAttribute> getBuildingAttributes() {
		return this.buildingAttributes;
	}

	public void setBuildingAttributes(List<BuildingAttribute> buildingAttributes) {
		this.buildingAttributes = buildingAttributes;
	}
}
