package org.flexpay.bti.service.importexport;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;

import java.util.Map;

public class BuildingAttributeData implements Cloneable {

	private Stub<BuildingAddress> buildingAddress;

	private String rowNum;
	private String streetName;
	private String streetType;
	private String district;
	private String buildingNumber;
	private String buildingBulk;
	private String externalId;
	private Map<String, String> name2Values = CollectionUtils.map();

	public Stub<BuildingAddress> getBuildingAddress() {
		return buildingAddress;
	}

	public void setBuildingAddress(Stub<BuildingAddress> buildingAddress) {
		this.buildingAddress = buildingAddress;
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

	public boolean isBuildingFound() {
		return buildingAddress != null;
	}

}
