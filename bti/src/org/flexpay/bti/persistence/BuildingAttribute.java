package org.flexpay.bti.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.DomainObject;

public class BuildingAttribute extends DomainObject {

	private BtiBuilding building;
	private BuildingAttributeType attributeType;

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BtiBuilding getBuilding() {
		return building;
	}

	public void setBuilding(BtiBuilding building) {
		this.building = building;
	}

	public BuildingAttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(BuildingAttributeType attributeType) {
		this.attributeType = attributeType;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("building", building).
				append("value", value).
				toString();
	}

}
