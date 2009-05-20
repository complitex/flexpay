package org.flexpay.bti.persistence.apartment;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

public class ApartmentAttribute extends ApartmentAttributeBase {

	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueForDate(Date td) {
		return value;
	}

	public void setValueForDates(String value, Date beginDt, Date endDt) {
		this.value = value;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("ApartmentAttribute {").
				append("id", getId()).
				append("value", value).
				append("}").toString();
	}

}
