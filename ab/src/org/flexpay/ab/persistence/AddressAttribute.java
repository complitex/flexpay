package org.flexpay.ab.persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.util.TranslationUtil;

import java.util.Locale;

/**
 * Building address attribute
 */
public class AddressAttribute extends DomainObjectWithStatus {

	private BuildingAddress buildingAddress;
	private AddressAttributeType addressAttributeType;
	private String value;

	public BuildingAddress getBuildings() {
		return this.buildingAddress;
	}

	public void setBuildings(BuildingAddress buildingAddress) {
		this.buildingAddress = buildingAddress;
	}

	public AddressAttributeType getBuildingAttributeType() {
		return this.addressAttributeType;
	}

	public void setBuildingAttributeType(AddressAttributeType addressAttributeType) {
		this.addressAttributeType = addressAttributeType;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String format(Locale locale, boolean shortMode) throws FlexPayException {

		StringBuilder result = new StringBuilder();
		AddressAttributeTypeTranslation typeTranslation = TranslationUtil
				.getTranslation(addressAttributeType.getTranslations(), locale);

		result.append(typeTranslation == null ? ""
											  : (shortMode ? getShortTypeName(locale)
														   : typeTranslation.getName() + " "));
		result.append(value);

		return result.toString();
	}

	private String getShortTypeName(Locale locale) {

		AddressAttributeTypeTranslation typeTranslation = TranslationUtil
				.getTranslation(addressAttributeType.getTranslations(), locale);
		AddressAttributeTypeTranslation defaultTranslation = TranslationUtil
				.getTranslation(addressAttributeType.getTranslations());

		StringBuilder buf = new StringBuilder();
		if (typeTranslation != null && StringUtils.isNotBlank(typeTranslation.getShortName())) {
			return typeTranslation.getShortName() + ". ";
		}
		if (defaultTranslation != null && StringUtils.isNotBlank(defaultTranslation.getShortName())) {
			return defaultTranslation.getShortName() + ". ";
		}

		return "";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof AddressAttribute)) {
			return false;
		}

		AddressAttribute that = (AddressAttribute) obj;
		return new EqualsBuilder()
				.append(value, that.getValue())
				.append(addressAttributeType, that.getBuildingAttributeType())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(value)
				.append(addressAttributeType)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("status", getStatus())
				.append("value", value)
				.append("type", addressAttributeType)
				.toString();
	}

}
