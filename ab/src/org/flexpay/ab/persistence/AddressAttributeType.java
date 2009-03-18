package org.flexpay.ab.persistence;

import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

/**
 * Building address attribute type
 */
public class AddressAttributeType extends DomainObjectWithStatus {

	private Set<AddressAttributeTypeTranslation> translations = Collections.emptySet();

	public AddressAttributeType() {
	}

	public AddressAttributeType(Long id) {
		super(id);
	}

	public AddressAttributeType(@NotNull Stub<AddressAttributeType> stub) {
		super(stub.getId());
	}

	/**
	 * Getter for property 'translations'.
	 *
	 * @return Value for property 'translations'.
	 */
	public Set<AddressAttributeTypeTranslation> getTranslations() {
		return translations;
	}

	/**
	 * Setter for property 'translations'.
	 *
	 * @param translations Value to set for property 'translations'.
	 */
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

	public void setTranslation(@NotNull AddressAttributeTypeTranslation translation) {
		translations = TranslationUtil.setTranslation(translations, this, translation);
	}

	/**
	 * Get type translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public AddressAttributeTypeTranslation getTranslation(@NotNull Language lang) {

		for (AddressAttributeTypeTranslation translation : getTranslations()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}
}
