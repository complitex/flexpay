package org.flexpay.common.persistence;

import java.util.Date;

public abstract class ValueObject extends DomainObject {

	public static final int TYPE_BOOLEAN = 1;
	public static final int TYPE_INT = 2;
	public static final int TYPE_LONG = 3;
	public static final int TYPE_STRING = 4;
	public static final int TYPE_DATE = 5;
	public static final int TYPE_DOUBLE = 6;

	private Boolean boolValue;
	private Integer intValue;
	private Long longValue;
	private String stringValue;
	private Date dateValue;
	private Double doubleValue;
	private int valueType;

	public Boolean isBoolValue() {
		return boolValue;
	}

	public void setBoolValue(Boolean boolValue) {
		this.boolValue = boolValue;
		valueType = TYPE_BOOLEAN;
	}

	public Integer getIntValue() {
		return intValue;
	}

	public void setIntValue(Integer intValue) {
		this.intValue = intValue;
		valueType = TYPE_INT;
	}

	public Long getLongValue() {
		return longValue;
	}

	public void setLongValue(Long longValue) {
		this.longValue = longValue;
		valueType = TYPE_LONG;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
		valueType = TYPE_STRING;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
		valueType = TYPE_DATE;
	}

	public Double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
		valueType = TYPE_DOUBLE;
	}

	public int getValueType() {
		return valueType;
	}

	public boolean isBool() {
		return valueType == TYPE_BOOLEAN;
	}

	public boolean isInt() {
		return valueType == TYPE_INT;
	}

	public boolean isLong() {
		return valueType == TYPE_LONG;
	}

	public boolean isString() {
		return valueType == TYPE_STRING;
	}

	public boolean isDate() {
		return valueType == TYPE_DATE;
	}

	public boolean isDouble() {
		return valueType == TYPE_DOUBLE;
	}

	@SuppressWarnings ({"UnusedDeclaration"})
	private void setValueType(int valueType) {
		this.valueType = valueType;
	}
}
