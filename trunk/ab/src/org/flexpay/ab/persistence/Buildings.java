package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.*;

/**
 * Buildings is a logical relation between building and several street addresses
 */
public class Buildings extends DomainObjectWithStatus {

	private Street street;
	private Building building;
	private Set<BuildingAttribute> buildingAttributes = Collections.emptySet();
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

	public Set<BuildingAttribute> getBuildingAttributes() {
		return this.buildingAttributes;
	}

	public void setBuildingAttributes(Set<BuildingAttribute> buildingAttributes) {
		this.buildingAttributes = buildingAttributes;
	}
	
	public BuildingAttribute getAttribute(BuildingAttributeType type) {
		for (BuildingAttribute attribute : buildingAttributes) {
			if (attribute.getBuildingAttributeType().equals(type)) {
				return attribute;
			}
		}
		
		return null;
	}
	
	/**
	 * Get building attribute
	 *
	 * @return BuildingAttribute number if attribute specified, or
	 *         <code>null</code> otherwise
	 */
	public BuildingAttribute getNumberAttribute() {
		for (BuildingAttribute attribute : buildingAttributes) {
			if (attribute.getBuildingAttributeType().isBuildingNumber()) {
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
			if (attribute.getBuildingAttributeType().isBulkNumber()) {
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

	public BuildingAttribute setBuildingAttribute(String value, BuildingAttributeType type) {
		BuildingAttribute attribute = null;
		for (BuildingAttribute attr : buildingAttributes) {
			if (type.equals(attr.getBuildingAttributeType())) {
				attribute = attr;
				break;
			}
		}

		if (StringUtils.isEmpty(value)) {
			if (attribute != null) {
				buildingAttributes.remove(attribute);
			}
			return attribute;
		}

		if (attribute == null) {
			attribute = new BuildingAttribute();
			attribute.setBuildingAttributeType(type);
			attribute.setBuildings(this);
			if (buildingAttributes == Collections.EMPTY_SET) {
				buildingAttributes = new HashSet<BuildingAttribute>();
			}
			buildingAttributes.add(attribute);
		}
		attribute.setValue(value);
		return attribute;
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
	 * @param primaryStatus the primaryStatus to set
	 */
	public void setPrimaryStatus(Boolean primaryStatus) {
		this.primaryStatus = primaryStatus;
	}
}
