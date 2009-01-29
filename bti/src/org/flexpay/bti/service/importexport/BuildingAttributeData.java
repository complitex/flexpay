package org.flexpay.bti.service.importexport;

import org.flexpay.bti.persistence.BtiBuilding;
import org.flexpay.common.util.CollectionUtils;

import java.util.Set;
import java.util.Map;

public class BuildingAttributeData implements Cloneable {

	private BtiBuilding building;

	private String rowNum;
	private String streetName;
	private String streetType;
	private String district;
	private String buildingNumber;
	private String buildingBulk;
	private String externalId;
	private Map<String, String> name2Values = CollectionUtils.map();

	public BtiBuilding getBuilding() {
		return building;
	}

	public void setBuilding(BtiBuilding building) {
		this.building = building;
	}

	public String getRowNum() {
		return rowNum;
	}

	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetType() {
		return streetType;
	}

	public void setStreetType(String streetType) {
		this.streetType = streetType;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getBuildingNumber() {
		return buildingNumber;
	}

	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public String getBuildingBulk() {
		return buildingBulk;
	}

	public void setBuildingBulk(String buildingBulk) {
		this.buildingBulk = buildingBulk;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public Map<String, String> getName2Values() {
		return name2Values;
	}

	public void setName2Values(Map<String, String> name2Values) {
		this.name2Values = name2Values;
	}

	public void addValue(String name, String value) {
		name2Values.put(name, value);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
