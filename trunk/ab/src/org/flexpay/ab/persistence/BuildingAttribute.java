package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * BuildingAttribute
 */
public class BuildingAttribute extends DomainObject {

	private Buildings buildings;
	private BuildingAttributeType buildingAttributeType;
	private String value;

	/**
	 * Constructs a new BuildingAttribute.
	 */
	public BuildingAttribute() {
	}

	/**
	 * Getter for property 'buildings'.
	 *
	 * @return Value for property 'buildings'.
	 */
	public Buildings getBuildings() {
		return this.buildings;
	}

	/**
	 * Setter for property 'buildings'.
	 *
	 * @param buildings Value to set for property 'buildings'.
	 */
	public void setBuildings(Buildings buildings) {
		this.buildings = buildings;
	}

	/**
	 * Getter for property 'buildingAttributeType'.
	 *
	 * @return Value for property 'buildingAttributeType'.
	 */
	public BuildingAttributeType getBuildingAttributeType() {
		return this.buildingAttributeType;
	}

	/**
	 * Setter for property 'buildingAttributeType'.
	 *
	 * @param buildingAttributeType Value to set for property 'buildingAttributeType'.
	 */
	public void setBuildingAttributeType(BuildingAttributeType buildingAttributeType) {
		this.buildingAttributeType = buildingAttributeType;
	}

	/**
	 * Getter for property 'value'.
	 *
	 * @return Value for property 'value'.
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Setter for property 'value'.
	 *
	 * @param value Value to set for property 'value'.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof BuildingAttribute)) {
			return false;
		}

		BuildingAttribute that = (BuildingAttribute) obj;
		return new EqualsBuilder()
				.append(value, that.getValue())
				.append(buildingAttributeType, that.getBuildingAttributeType())
				.isEquals();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("value", value)
				.append("type", buildingAttributeType.getType())
				.toString();
	}
}
