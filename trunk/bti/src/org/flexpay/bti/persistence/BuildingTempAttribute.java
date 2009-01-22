package org.flexpay.bti.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

/**
 * Time dependent building attribute
 */
public class BuildingTempAttribute extends DomainObject {

	private BtiBuilding building;

	private Date begin;
	private Date end;

	private String name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("building", building).
				append("begin", begin).
				append("end", end).
				append("name", name).
				append("value", value).
				toString();
	}
}
