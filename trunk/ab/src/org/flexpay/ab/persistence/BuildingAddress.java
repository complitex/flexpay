package org.flexpay.ab.persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

/**
 * Buildings is a logical relation between building and several street addresses
 */
public class BuildingAddress extends DomainObjectWithStatus {

	private Street street;
	private Building building;
	private Set<AddressAttribute> addressAttributes = Collections.emptySet();
	private boolean primaryStatus;

	public BuildingAddress() {
	}

	public BuildingAddress(Long id) {
		super(id);
	}

	public BuildingAddress(Stub<BuildingAddress> stub) {
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
	public Set<AddressAttribute> getBuildingAttributes() {
		return addressAttributes;
	}

	@SuppressWarnings ({"UnusedDeclaration"})
	private void setBuildingAttributes(Set<AddressAttribute> addressAttributes) {
		this.addressAttributes = addressAttributes;
	}

	@Nullable
	public AddressAttribute getAttribute(@NotNull AddressAttributeType type) {

		for (AddressAttribute attribute : addressAttributes) {
			if (type.equals(attribute.getBuildingAttributeType())) {
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
	public AddressAttribute getNumberAttribute() {

		for (AddressAttribute attribute : addressAttributes) {
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
	public AddressAttribute getBulkAttribute() {
		for (AddressAttribute attribute : addressAttributes) {
			if (attribute.getBuildingAttributeType().isBulkNumber()) {
				return attribute;
			}
		}

		return null;
	}

	/**
	 * Get building attribute
	 *
	 * @return BuildingAttribute part if attribute specified, or <code>null</code> otherwise
	 */
	@Nullable
	public AddressAttribute getPartAttribute() {
		for (AddressAttribute attribute : addressAttributes) {
			if (attribute.getBuildingAttributeType().isPartNumber()) {
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
		AddressAttribute attribute = getNumberAttribute();
		return attribute == null ? null : attribute.getValue();
	}

	/**
	 * Get building optional bulk number
	 *
	 * @return Building number if attribute specified, or <code>null</code> otherwise
	 */
	@Nullable
	public String getBulk() {
		AddressAttribute attribute = getBulkAttribute();
		return attribute == null ? null : attribute.getValue();
	}

	public AddressAttribute setBuildingAttribute(@NotNull AddressAttribute attribute) {
		return setBuildingAttribute(
				attribute.getValue(), attribute.getBuildingAttributeType());
	}

	public AddressAttribute setBuildingAttribute(String value, AddressAttributeType type) {
		AddressAttribute attribute = null;
		for (AddressAttribute attr : addressAttributes) {
			if (type.equals(attr.getBuildingAttributeType())) {
				attribute = attr;
				break;
			}
		}

		if (StringUtils.isEmpty(value)) {
			if (attribute != null) {
				addressAttributes.remove(attribute);
			}
			return attribute;
		}

		if (attribute == null) {
			attribute = new AddressAttribute();
			attribute.setBuildingAttributeType(type);
			attribute.setBuildings(this);
			//noinspection CollectionsFieldAccessReplaceableByMethodCall
			if (addressAttributes == Collections.EMPTY_SET) {
				addressAttributes = CollectionUtils.set();
			}
			addressAttributes.add(attribute);
		}
		attribute.setValue(value);
		return attribute;
	}

	public String format(Locale locale, boolean shortMode) throws FlexPayException {

		StringBuilder result = new StringBuilder();
		AddressAttribute attribute = getNumberAttribute();
		if (attribute != null) {
			result.append(attribute.format(locale, shortMode));
		}
		attribute = getBulkAttribute();
		if (attribute != null) {
			result.append(", ").
					append(attribute.format(locale, shortMode));
		}
		attribute = getPartAttribute();
		if (attribute != null) {
			result.append(", ").
					append(attribute.format(locale, shortMode));
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
	 * Check if address is a primary one
	 *
	 * @return <code>true</code> if address is a primary one, or <code>false</code> otherwise
	 */
	public boolean isPrimary() {
		return primaryStatus;
	}

	/**
	 * Check if address is not a primary one
	 *
	 * @return <code>false</code> if address is a primary one, or <code>true</code> otherwise
	 */
	public boolean isNotPrimary() {
		return !isPrimary();
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
	public boolean hasSameAttributes(Set<AddressAttribute> attrs) {
		if (addressAttributes.size() != attrs.size()) {
			return false;
		}

		for (AddressAttribute attr : attrs) {
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
	public boolean hasSameAttributeValue(@NotNull AddressAttribute attr) {
		for (AddressAttribute attribute : addressAttributes) {
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
	public static AddressAttribute numberAttribute(@NotNull String value) {
		AddressAttribute attribute = new AddressAttribute();
		attribute.setValue(value);
		attribute.setBuildingAttributeType(ApplicationConfig.getBuildingAttributeTypeNumber());
		return attribute;
	}

	@NotNull
	public static AddressAttribute bulkAttribute(@NotNull String value) {
		AddressAttribute attribute = new AddressAttribute();
		attribute.setValue(value);
		attribute.setBuildingAttributeType(ApplicationConfig.getBuildingAttributeTypeBulk());
		return attribute;
	}

	@NotNull
	public Stub<Building> getBuildingStub() {
		return stub(building);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				append("bulk", getBulk()).
				append("number", getNumber()).
				append("primaryStatus", primaryStatus).
				append("streetId", street != null ? street.getId() : null).
				append("buildingId", building != null ? building.getId() : null).
				toString();
	}

}
