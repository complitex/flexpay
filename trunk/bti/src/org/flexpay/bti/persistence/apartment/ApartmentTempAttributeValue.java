package org.flexpay.bti.persistence.apartment;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class ApartmentTempAttributeValue extends DomainObject implements Comparable<ApartmentTempAttributeValue> {

	private ApartmentTempAttribute attribute;
	private String value;
	private Date begin;
	private Date end;

	public ApartmentTempAttributeValue() {
	}

	public ApartmentTempAttributeValue(@NotNull Long id) {
		super(id);
	}

	public ApartmentTempAttributeValue(String value, Date begin, Date end) {
		this.value = value;
		this.begin = begin;
		this.end = end;
	}

	public ApartmentTempAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(ApartmentTempAttribute attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public int compareTo(ApartmentTempAttributeValue o) {
		return begin.compareTo(o.begin);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("ApartmentTempAttributeValue {").
				append("id", getId()).
				append("value", value).
				append("begin", begin).
				append("end", end).
				append("}").toString();
	}

}
