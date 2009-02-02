package org.flexpay.bti.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * Simple building attribute type
 */
public abstract class BuildingAttributeType extends DomainObjectWithStatus {

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

	public BuildingAttributeGroup getGroup() {
		return group;
	}

	public void setGroup(BuildingAttributeGroup group) {
		this.group = group;
	}

	public Set<BuildingAttributeTypeName> getTranslations() {
		return translations;
	}

	@SuppressWarnings ({"UnusedDeclaration"})
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

}
