package org.flexpay.ab.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DomainObjectWithStatus;

/**
 * Buildings is a logical relation between building and several street addresses
 */
public class Buildings extends DomainObjectWithStatus {

	private Street street;
	private Building building;
	private List<BuildingAttribute> buildingAttributes = Collections
			.emptyList();
	private Boolean primaryStatus;

	public Buildings() {
	}

	public Buildings(Long id) {
		super(id);
	}

	public Street getStreet() {
		return this.street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

	public Building getBuilding() {
		return this.building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public List<BuildingAttribute> getBuildingAttributes() {
		return this.buildingAttributes;
	}

	public void setBuildingAttributes(List<BuildingAttribute> buildingAttributes) {
		this.buildingAttributes = buildingAttributes;
	}

	/**
	 * Get building attribute
	 * 
	 * @return BuildingAttribute number if attribute specified, or
	 *         <code>null</code> otherwise
	 */
	public BuildingAttribute getNumberAttribute() {
		for (BuildingAttribute attribute : buildingAttributes) {
			if (attribute != null
					&& attribute.getBuildingAttributeType().isBuildingNumber()) {
				return attribute;
			}
		}

		return null;
	}

	/**
	 * Get building attribute
	 * 
	 * @return BuildingAttribute bulk if attribute specified, or
	 *         <code>null</code> otherwise
	 */
	public BuildingAttribute getBulkAttribute() {
		for (BuildingAttribute attribute : buildingAttributes) {
			if (attribute != null
					&& attribute.getBuildingAttributeType().isBulkNumber()) {
				return attribute;
			}
		}

		return null;
	}

	/**
	 * Get building number
	 * 
	 * @return Building number if attribute specified, or <code>null</code>
	 *         otherwise
	 */
	public String getNumber() {
		BuildingAttribute attribute = getNumberAttribute();
		return attribute == null ? null : attribute.getValue();
	}

	public void setBuildingAttribute(String value, BuildingAttributeType type) {
		BuildingAttribute attribute = null;
		for (BuildingAttribute attr : buildingAttributes) {
			if (attr != null && type.equals(attr.getBuildingAttributeType())) {
				attribute = attr;
				break;
			}
		}

		if (value == null || "".equals(value)) {
			if (attribute != null) {
				buildingAttributes.remove(attribute);
			}
			return;
		}

		if (attribute == null) {
			attribute = new BuildingAttribute();
			attribute.setBuildingAttributeType(type);
			attribute.setBuildings(this);
			if (buildingAttributes.isEmpty()) {
				buildingAttributes = new ArrayList<BuildingAttribute>();
			}
			buildingAttributes.add(attribute);
		}
		attribute.setValue(value);
	}

	/**
	 * Get building optional bulk number
	 * 
	 * @return Building number if attribute specified, or <code>null</code>
	 *         otherwise
	 */
	public String getBulk() {
		BuildingAttribute attribute = getBulkAttribute();
		return attribute == null ? null : attribute.getValue();
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append(
				"Building id", building.getId()).append("Street id",
				street.getId()).append("attributes: ",
				buildingAttributes.toArray()).toString();
	}

	public String format(Locale locale, boolean shortMode)
			throws FlexPayException {
		StringBuilder result = new StringBuilder();
		BuildingAttribute attribute = getNumberAttribute();
		if (attribute != null) {
			result.append(attribute.format(locale, shortMode));
		}
		attribute = getBulkAttribute();
		if (attribute != null) {
			result.append(", ");
			result.append(attribute.format(locale, shortMode));
		}

		return result.toString();
	}

	/**
	 * @return the primaryStatus
	 */
	public Boolean getPrimaryStatus() {
		return primaryStatus;
	}

	/**
	 * @param primaryStatus
	 *            the primaryStatus to set
	 */
	public void setPrimaryStatus(Boolean primaryStatus) {
		this.primaryStatus = primaryStatus;
	}
}
