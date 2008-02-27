package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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

	public Buildings(Long id) {
		super(id);
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

	/**
	 * Get building number
	 *
	 * @return Building number if attribute specified, or <code>null</code> otherwise
	 */
	public String getNumber() {
		for (BuildingAttribute attribute : buildingAttributes) {
			if (attribute != null && attribute.getBuildingAttributeType().isBuildingNumber()) {
				return attribute.getValue();
			}
		}

		return null;
	}

	/**
	 * Get building optional bulk number
	 *
	 * @return Building number if attribute specified, or <code>null</code> otherwise
	 */
	public String getBulk() {
		for (BuildingAttribute attribute : buildingAttributes) {
			if (attribute != null && attribute.getBuildingAttributeType().isBulkNumber()) {
				return attribute.getValue();
			}
		}

		return null;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("Building id", building.getId())
				.append("Street id", street.getId())
				.append("attributes: ", buildingAttributes.toArray())
				.toString();
	}
}
