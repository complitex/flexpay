package org.flexpay.bti.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

/**
 * Time dependent building attribute
 */
public class BuildingTempAttribute extends BuildingAttribute {

	private Date begin;
	private Date end;

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

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("building", getBuilding()).
				append("begin", begin).
				append("end", end).
				append("value", getValue()).
				toString();
	}
}
