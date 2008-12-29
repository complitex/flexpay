package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.DomainObject;

import java.util.Date;

/**
 * BuildingStatus representation of current building status, like "Building started",
 * "Rebuilding", etc
 */
public class BuildingStatus extends DomainObject {

	private Building building;
	private Date begin;
	private Date end;
	private String value;

	public BuildingStatus() {
	}

	public Building getBuilding() {
		return this.building;
	}

	public void setBuilding(Building building) {
		this.building = building;
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

}


