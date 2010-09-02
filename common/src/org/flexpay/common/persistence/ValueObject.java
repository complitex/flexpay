package org.flexpay.common.persistence;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ValueObject extends DomainObject {

	public static final int TYPE_BOOLEAN = 1;
	public static final int TYPE_INT = 2;
	public static final int TYPE_LONG = 3;
	public static final int TYPE_STRING = 4;
	public static final int TYPE_DATE = 5;
	public static final int TYPE_DOUBLE = 6;
	public static final int TYPE_DECIMAL = 7;

    public static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

	private Boolean boolValue;
	private Integer intValue;
	private Long longValue;
	private String stringValue;
	private Date dateValue;
	private Double doubleValue;
	private BigDecimal decimalValue;
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

	public BigDecimal getDecimalValue() {
		return decimalValue;
	}

	public void setDecimalValue(BigDecimal decimalValue) {
		this.decimalValue = decimalValue;
		valueType = TYPE_DECIMAL;
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

	public boolean isDecimal() {
		return valueType == TYPE_DECIMAL;
	}

	@SuppressWarnings ({"UnusedDeclaration"})
	private void setValueType(int valueType) {
		this.valueType = valueType;
	}

	/**
	 * Update object value, type should be the same as set before
	 *
	 * @param obj Object of type that matches current <code>valueType</code>
	 */
	public void updateValue(Object obj) {
		switch (valueType) {
			case TYPE_BOOLEAN:
				setBoolValue((Boolean) obj);
				break;
			case TYPE_DATE:
				setDateValue((Date) obj);
				break;
			case TYPE_DECIMAL:
				setDecimalValue((BigDecimal) obj);
				break;
			case TYPE_DOUBLE:
				setDoubleValue((Double) obj);
				break;
			case TYPE_INT:
				setIntValue((Integer) obj);
				break;
			case TYPE_LONG:
				setLongValue((Long) obj);
				break;
			case TYPE_STRING:
				setStringValue((String) obj);
				break;
			default:
				throw new IllegalStateException("updateValue called before valueType set");
		}
	}

    /**
     * Update object value. Setting String value and parsing in this method
     *
     * @param value String value whitch will be parsed
     */
    public void setValue(String value) throws ParseException {
        switch (valueType) {
            case TYPE_BOOLEAN:
                setBoolValue(Boolean.valueOf(value));
                break;
            case TYPE_DATE:
                setDateValue(DEFAULT_DATE_FORMAT.parse(value));
                break;
            case TYPE_DECIMAL:
                setDecimalValue(new BigDecimal(value));
                break;
            case TYPE_DOUBLE:
                setDoubleValue(Double.valueOf(value));
                break;
            case TYPE_INT:
                setIntValue(Integer.valueOf(value));
                break;
            case TYPE_LONG:
                setLongValue(Long.valueOf(value));
                break;
            case TYPE_STRING:
                setStringValue(value);
                break;
            default:
                throw new IllegalStateException("updateValue called before valueType set");
        }
    }

	public Object value() {
		switch (valueType) {
			case TYPE_BOOLEAN:
				return isBoolValue();
			case TYPE_DATE:
				return getDateValue();
			case TYPE_DECIMAL:
				return getDecimalValue();
			case TYPE_DOUBLE:
				return getDoubleValue();
			case TYPE_INT:
				return getIntValue();
			case TYPE_LONG:
				return getLongValue();
			case TYPE_STRING:
				return getStringValue();
			default:
				return null;
		}
	}

	public boolean sameValue(ValueObject object) {
		return valueType == object.getValueType() &&
			   ObjectUtils.equals(value(), object.value());
	}

	public boolean empty() {
		Object value = value();
		return value == null || (isString() && StringUtils.isEmpty(getStringValue()));
	}

	public boolean notEmpty() {
		return !empty();
	}

	@Override
	public String toString() {
		return buildToString(new ToStringBuilder(this).
				append("id", getId()).
				append("boolValue", boolValue).
				append("intValue", intValue).
				append("longValue", longValue).
				append("stringValue", stringValue).
				append("dateValue", dateValue).
				append("doubleValue", doubleValue).
				append("valueType", valueType)).
				toString();
	}

	protected abstract ToStringBuilder buildToString(ToStringBuilder builder);
}
