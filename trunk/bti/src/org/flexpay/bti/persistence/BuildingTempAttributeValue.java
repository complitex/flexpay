package org.flexpay.bti.persistence;

import org.flexpay.common.persistence.DomainObject;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class BuildingTempAttributeValue extends DomainObject implements Comparable<BuildingTempAttributeValue> {

	private BuildingTempAttribute attribute;
	private String value;
	private Date begin;
	private Date end;

	/**
	 * Constructs a new DomainObject.
	 */
	public BuildingTempAttributeValue() {
	}

	public BuildingTempAttributeValue(@NotNull Long id) {
		super(id);
	}

	/**
	 * Constructs a new DomainObject.
	 */
	public BuildingTempAttributeValue(String value, Date begin, Date end) {
		this.value = value;
		this.begin = begin;
		this.end = end;
	}

	public BuildingTempAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(BuildingTempAttribute attribute) {
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

	public int compareTo(BuildingTempAttributeValue o) {
		return begin.compareTo(o.begin);
	}
}
