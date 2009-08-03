package org.flexpay.bti.persistence.apartment;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.flexpay.common.persistence.TemporalValueObject;

public class ApartmentAttribute extends TemporalValueObject implements Comparable<ApartmentAttribute> {

	private int isTemporal = 0;
	private BtiApartment apartment;
	private ApartmentAttributeType attributeType;

	public int getTemporal() {
		return isTemporal;
	}

	public void setTemporal(int temporal) {
		isTemporal = temporal;
	}

	public BtiApartment getApartment() {
		return apartment;
	}

	public void setApartment(BtiApartment apartment) {
		this.apartment = apartment;
	}

	public ApartmentAttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(ApartmentAttributeType attributeType) {
		this.attributeType = attributeType;
	}

	@Override
	public int compareTo(ApartmentAttribute o) {
		return getBegin().compareTo(o.getBegin());
	}

	@Override
	protected ToStringBuilder buildToString(ToStringBuilder builder) {
		return super.buildToString(builder)
				.append("type-id", attributeType == null ? null : attributeType.getId())
				.append("apartment-id", apartment == null ? null : apartment.getId());
	}

	public ApartmentAttribute copy() {

		ApartmentAttribute attribute = new ApartmentAttribute();
		attribute.setApartment(apartment);
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
}
