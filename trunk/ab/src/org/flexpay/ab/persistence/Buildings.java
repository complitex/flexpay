package org.flexpay.ab.persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Buildings is a logical relation between building and several street addresses
 */
public class Buildings extends DomainObjectWithStatus {

	private Street street;
	private Building building;
	private Set<BuildingAttribute> buildingAttributes = Collections.emptySet();
	private boolean primaryStatus;

	public Buildings() {
	}

	public Buildings(Long id) {
		super(id);
	}

	public Buildings(Stub<Buildings> stub) {
		super(stub.getId());
	}

	@NotNull
	public Street getStreet() {
		return this.street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

	@NotNull
	public Building getBuilding() {
		return this.building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	@NotNull
	public Set<BuildingAttribute> getBuildingAttributes() {
		return this.buildingAttributes;
	}

	public void setBuildingAttributes(Set<BuildingAttribute> buildingAttributes) {
		this.buildingAttributes = buildingAttributes;
	}

	@Nullable
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
	 * @return BuildingAttribute number if attribute specified, or <code>null</code> otherwise
	 */
	@Nullable
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
	 * @return BuildingAttribute bulk if attribute specified, or <code>null</code> otherwise
	 */
	@Nullable
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
	 * @return Building number if attribute specified, or <code>null</code> otherwise
	 */
	@Nullable
	public String getNumber() {
		BuildingAttribute attribute = getNumberAttribute();
		return attribute == null ? null : attribute.getValue();
	}

	/**
	 * Get building optional bulk number
	 *
	 * @return Building number if attribute specified, or <code>null</code> otherwise
	 */
	@Nullable
	public String getBulk() {
		BuildingAttribute attribute = getBulkAttribute();
		return attribute == null ? null : attribute.getValue();
	}

	public BuildingAttribute setBuildingAttribute(@NotNull BuildingAttribute attribute) {
		return setBuildingAttribute(
				attribute.getValue(), attribute.getBuildingAttributeType());
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

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("Building id", building.getId())
				.append("Street id", street.getId())
				.append("attributes: ", buildingAttributes.toArray())
				.toString();
	}

	public String format(Locale locale, boolean shortMode) throws FlexPayException {

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
	public boolean getPrimaryStatus() {
		return primaryStatus;
	}

	/**
	 * Check if buildings is a primary one
	 *
	 * @return <code>true</code> if buildings is a primary one, or <code>false</code> otherwise
	 */
	public boolean isPrimary() {
		return primaryStatus;
	}

	/**
	 * @param primaryStatus the primaryStatus to set
	 */
	public void setPrimaryStatus(boolean primaryStatus) {
		this.primaryStatus = primaryStatus;
	}

	/**
	 * Check if object attributes has the same values as in set
	 *
	 * @param attrs Set of attributes
	 * @return <code>true</code> if all of attrs has the same value in attributes
	 */
	public boolean hasSameAttributes(Set<BuildingAttribute> attrs) {
		if (buildingAttributes.size() != attrs.size()) {
			return false;
		}

		for (BuildingAttribute attr : attrs) {
			if (!hasSameAttributeValue(attr)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Check if object attributes contains the same value
	 *
	 * @param attr Attribute to check
	 * @return <code>true</code> if there is an attribute having the same value
	 */
	public boolean hasSameAttributeValue(@NotNull BuildingAttribute attr) {
		for (BuildingAttribute attribute : buildingAttributes) {
			if (attr.equals(attribute)) {
				return true;
			}
		}

		return false;
	}

	@NotNull
	public Stub<Street> getStreetStub() {
		return stub(getStreet());
	}

	@NotNull
	public District getDistrict() {
		return getBuilding().getDistrict();
	}

	@NotNull
	public Stub<District> getDistrictStub() {
		return stub(getDistrict());
	}

	@NotNull
	public static BuildingAttribute numberAttribute(@NotNull String value) {
		BuildingAttribute attribute = new BuildingAttribute();
		attribute.setValue(value);
		attribute.setBuildingAttributeType(ApplicationConfig.getBuildingAttributeTypeNumber());
		return attribute;
	}

	@NotNull
	public static BuildingAttribute bulkAttribute(@NotNull String value) {
		BuildingAttribute attribute = new BuildingAttribute();
		attribute.setValue(value);
		attribute.setBuildingAttributeType(ApplicationConfig.getBuildingAttributeTypeBulk());
		return attribute;
	}

	@NotNull
	public Stub<Building> getBuildingStub() {
		return stub(building);
	}
}
