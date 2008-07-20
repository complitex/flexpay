package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.util.DateUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

/**
 * ApartmentNumber
 */
public class ApartmentNumber extends DomainObject {

	private Apartment apartment;
	private Date begin;
	private Date end;
	private String value;

	public ApartmentNumber() {
	}

	public Apartment getApartment() {
		return this.apartment;
	}

	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	public Date getBegin() {
		return this.begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return this.end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("number", value)
				.append("begin", DateUtil.format(begin))
				.append("end", DateUtil.format(end))
				.toString();
	}
}
