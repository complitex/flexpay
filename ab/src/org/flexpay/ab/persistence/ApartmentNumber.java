package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.util.DateUtil;

import java.util.Date;

/**
 * ApartmentNumber
 */
public class ApartmentNumber extends DomainObject implements Comparable<ApartmentNumber> {

	private Apartment apartment;
	private Date begin;
	private Date end;
	private String value;

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

	@Override
	public int compareTo(ApartmentNumber o) {
		return begin.compareTo(o.begin);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("number", value)
				.append("begin", DateUtil.format(begin))
				.append("end", DateUtil.format(end))
				.toString();
	}

}
