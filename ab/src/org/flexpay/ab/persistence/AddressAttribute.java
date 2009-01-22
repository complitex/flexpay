package org.flexpay.ab.persistence;

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

	/**
	 * Constructs a new BuildingAttribute.
	 */
	public AddressAttribute() {
	}

	/**
	 * Getter for property 'buildings'.
	 *
	 * @return Value for property 'buildings'.
	 */
	public BuildingAddress getBuildings() {
		return this.buildingAddress;
	}

	/**
	 * Setter for property 'buildings'.
	 *
	 * @param buildingAddress Value to set for property 'buildings'.
	 */
	public void setBuildings(BuildingAddress buildingAddress) {
		this.buildingAddress = buildingAddress;
	}

	/**
	 * Getter for property 'buildingAttributeType'.
	 *
	 * @return Value for property 'buildingAttributeType'.
	 */
	public AddressAttributeType getBuildingAttributeType() {
		return this.addressAttributeType;
	}

	/**
	 * Setter for property 'buildingAttributeType'.
	 *
	 * @param addressAttributeType Value to set for property 'buildingAttributeType'.
	 */
	public void setBuildingAttributeType(AddressAttributeType addressAttributeType) {
		this.addressAttributeType = addressAttributeType;
	}

	/**
	 * Getter for property 'value'.
	 *
	 * @return Value for property 'value'.
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Setter for property 'value'.
	 *
	 * @param value Value to set for property 'value'.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
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

	public int hashCode() {
		return new HashCodeBuilder()
				.append(value).append(addressAttributeType)
				.append(addressAttributeType)
				.toHashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("value", value)
				.append("type", addressAttributeType)
				.toString();
	}

	public String format(Locale locale, boolean shortMode) throws FlexPayException {

		StringBuilder result = new StringBuilder();
		AddressAttributeTypeTranslation typeTranslation = TranslationUtil
				.getTranslation(addressAttributeType.getTranslations(), locale);

		result.append(typeTranslation == null ? ""
											  : (shortMode ? typeTranslation.getShortName() + ". "
														   : typeTranslation.getName() + " "));
		result.append(value);

		return result.toString();
	}
}
