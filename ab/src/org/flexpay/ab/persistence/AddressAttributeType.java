package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

/**
 * Building address attribute type
 */
public class AddressAttributeType extends DomainObjectWithStatus implements Comparable<AddressAttributeType> {

	private Set<AddressAttributeTypeTranslation> translations = set();

	public AddressAttributeType() {
	}

	public AddressAttributeType(Long id) {
		super(id);
	}

	public AddressAttributeType(@NotNull Stub<AddressAttributeType> stub) {
		super(stub.getId());
	}

	public Set<AddressAttributeTypeTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<AddressAttributeTypeTranslation> translations) {
		this.translations = translations;
	}

	/**
	 * Check if attribute type is a building number
	 *
	 * @return <code>true</code> if attribute type is a building number
	 */
	public boolean isBuildingNumber() {
		return equals(ApplicationConfig.getBuildingAttributeTypeNumber());
	}

	/**
	 * Check if attribute type is a bulk number
	 *
	 * @return <code>true</code> if attribute type is a bulk number
	 */
	public boolean isBulkNumber() {
		AddressAttributeType type = ApplicationConfig.getBuildingAttributeTypeBulk();
		return equals(type);
	}

	/**
	 * Check if attribute type is a part number
	 *
	 * @return <code>true</code> if attribute type is a part number
	 */
	public boolean isPartNumber() {
		AddressAttributeType type = ApplicationConfig.getBuildingAttributeTypePart();
		return equals(type);
	}

	public void setTranslation(AddressAttributeTypeTranslation translation) {
		translations = TranslationUtil.setTranslation(translations, this, translation);
	}

	/**
	 * Get type translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public AddressAttributeTypeTranslation getTranslation(Language lang) {

        if (lang == null) {
            return null;
        }

		for (AddressAttributeTypeTranslation translation : getTranslations()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}

	/**
	 * Get type translation in a default language
	 *
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public AddressAttributeTypeTranslation getDefaultTranslation() {
		return getTranslation(org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage());
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				toString();
	}

	@Override
	public int compareTo(AddressAttributeType o) {
		return -compareAscTo(o);
	}

	private int compareAscTo(AddressAttributeType o) {
		boolean oIsBuildingNumber = o.isBuildingNumber();
		boolean thisIsBuildingNumber = isBuildingNumber();
		if (oIsBuildingNumber != thisIsBuildingNumber) {
			return thisIsBuildingNumber ? 1 : -1;
		}
		boolean oIsBulkNumber = o.isBulkNumber();
		boolean thisIsBulkNumber = isBulkNumber();
		if (oIsBulkNumber != thisIsBulkNumber) {
			return thisIsBuildingNumber ? 1 : -1;
		}
		boolean oIsPartNumber = o.isPartNumber();
		boolean thisIsPartNumber = isPartNumber();
		if (oIsPartNumber != thisIsPartNumber) {
			return thisIsBuildingNumber ? 1 : -1;
		}

		Long id = getId();
		Long oId = o.getId();
		return id == null && oId == null ? 0 :
			   id == null ? -1 :
			   oId == null ? 1 :
			   id.compareTo(oId);
	}
}
