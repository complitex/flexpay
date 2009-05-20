package org.flexpay.bti.persistence.building;

import java.util.Date;

public class BuildingAttribute extends BuildingAttributeBase {

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

}
