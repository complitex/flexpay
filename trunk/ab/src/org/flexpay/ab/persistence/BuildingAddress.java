package org.flexpay.ab.persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.util.TranslationUtil;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.EsbXmlSyncObject;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.set;

/**
 * Buildings is a logical relation between building and several street addresses
 */
public class BuildingAddress extends EsbXmlSyncObject {

	private Street street;
	private Building building;
	private Set<AddressAttribute> addressAttributes = set();
	private boolean primaryStatus;

	public BuildingAddress() {
	}

	public BuildingAddress(Long id) {
		super(id);
	}

	public BuildingAddress(Stub<BuildingAddress> stub) {
		super(stub.getId());
	}

    @Override
    public String getXmlString() {

        StringBuilder builder = new StringBuilder();

        builder.append("    <buildingAddress>\n");

        if (ACTION_INSERT.equals(action) || ACTION_UPDATE.equals(action)) {

            builder.append("        <id>").append(id).append("</id>\n").
                    append("        <buildingId>").append(building.getId()).append("</buildingId>\n").
                    append("        <streetId>").append(street.getId()).append("</streetId>\n").
                    append("        <primary>").append(primaryStatus).append("</primary>\n").
                    append("        <attributes>\n");
            for (AddressAttribute attr : addressAttributes) {
                builder.append("            <org.flexpay.mule.request.MuleAddressAttribute>\n").
                        append("                <id>").append(attr.getBuildingAttributeType().getId()).append("</id>\n").
                        append("                <value>").append(attr.getValue()).append("</value>\n").
                        append("            </org.flexpay.mule.request.MuleAddressAttribute>\n");
            }
            builder.append("        </attributes>\n");
        } else if (ACTION_DELETE.equals(action)) {
            builder.append("        <buildingId>").append(building.getId()).append("</buildingId>\n").
                    append("        <ids>\n");
            for (Long id : ids) {
                builder.append("            <long>").append(id).append("</long>\n");
            }
            builder.append("        </ids>\n");
        } else if (ACTION_UPDATE_ADDRESS_SET_PRIMARY.equals(action)) {
            builder.append("        <id>").append(id).append("</id>\n").
                    append("        <buildingId>").append(building.getId()).append("</buildingId>\n");
        }

        builder.append("    </buildingAddress>\n");

        return builder.toString();
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
				addressAttributes = set();
			}
			addressAttributes.add(attribute);
		}
		attribute.setValue(value);
		return attribute;
	}

	public String format(Locale locale, boolean shortMode) throws FlexPayException {

		return TranslationUtil.getBuildingNumber(this, locale);		
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
	public Town getTown() {
		return getStreet().getTown();
	}

	@NotNull
	public Region getRegion() {
		return getTown().getRegion();
	}

	@NotNull
	public Country getCountry() {
		return getRegion().getCountry();
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
