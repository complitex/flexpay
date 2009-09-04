package org.flexpay.bti.persistence.building;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.TemporalValueObject;

public class BuildingAttribute extends TemporalValueObject implements Comparable<BuildingAttribute> {

	private int isTemporal = 0;
	private BtiBuilding building;
	private BuildingAttributeType attributeType;

	public int getTemporal() {
		return isTemporal;
	}

	public void setTemporal(int temporal) {
		isTemporal = temporal;
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
	public int compareTo(BuildingAttribute o) {
		return getBegin().compareTo(o.getBegin());
	}

	public BuildingAttribute copy() {

		BuildingAttribute attribute = new BuildingAttribute();
		attribute.setBuilding(building);
		attribute.setAttributeType(attributeType);
		attribute.setBegin(getBegin());
		attribute.setEnd(getEnd());
		attribute.setTemporal(getTemporal());
		if (isBool()) {
			attribute.setBoolValue(isBoolValue());
		} else if (isDate()) {
			attribute.setDateValue(getDateValue());
		} else if (isDecimal()) {
			attribute.setDecimalValue(getDecimalValue());
		} else if (isDouble()) {
			attribute.setDoubleValue(getDoubleValue());
		} else if (isInt()) {
			attribute.setIntValue(getIntValue());
		} else if (isLong()) {
			attribute.setLongValue(getLongValue());
		} else if (isString()) {
			attribute.setStringValue(getStringValue());
		}

		return attribute;
	}

	@Override
	protected ToStringBuilder buildToString(ToStringBuilder builder) {
		return super.buildToString(builder)
				.append("isTemporal", isTemporal)
				.append("type.id", attributeType == null ? null : attributeType.getId())
				.append("building.id", building == null ? null : building.getId());
	}

}
