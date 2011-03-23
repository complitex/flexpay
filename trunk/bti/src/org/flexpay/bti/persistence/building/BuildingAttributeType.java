package org.flexpay.bti.persistence.building;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

/**
 * Simple building attribute type
 */
public abstract class BuildingAttributeType extends DomainObjectWithStatus {

	private int isTemporal;
	private String uniqueCode;
	private BuildingAttributeGroup group;
	private Set<BuildingAttributeTypeName> translations = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public BuildingAttributeType() {
	}

	public BuildingAttributeType(@NotNull Long id) {
		super(id);
	}

	public BuildingAttributeType(@NotNull Stub<BuildingAttributeType> stub) {
		super(stub.getId());
	}

	/**
	 * Check if this attribute type is for temporal attributes
	 *
	 * @return <code>true</code> if type is temporal, or <code>false</code> otherwise
	 */
	public boolean isTemp() {
		return isTemporal != 0;
	}

	public void setTemp(boolean tmp) {
		isTemporal = tmp ? 1 : 0;
	}

	public BuildingAttributeGroup getGroup() {
		return group;
	}

	public void setGroup(BuildingAttributeGroup group) {
		this.group = group;
	}

	public Set<BuildingAttributeTypeName> getTranslations() {
		return translations;
	}

	private void setTranslations(Set<BuildingAttributeTypeName> translations) {
		this.translations = translations;
	}

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	/**
	 * Perform attribute validation
	 *
	 * @param value Attribute value to validate
	 * @throws FlexPayException if validation fails
	 */
	abstract public void validate(String value) throws FlexPayException;

	/**
	 * Get type name code
	 *
	 * @return type name code
	 */
	abstract public String getI18nTitle();

	public void setTranslation(BuildingAttributeTypeName name) {
		translations = TranslationUtil.setTranslation(translations, this, name);
	}

	/**
	 * Get type translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public BuildingAttributeTypeName getTranslation(@NotNull Language lang) {

		for (BuildingAttributeTypeName translation : getTranslations()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}

}
