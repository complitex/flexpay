package org.flexpay.bti.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.exception.FlexPayException;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * Simple building attribute type
 */
public abstract class BuildingAttributeType extends DomainObjectWithStatus {

	Set<BuildingAttributeTypeName> translations = Collections.emptySet();

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

	public Set<BuildingAttributeTypeName> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<BuildingAttributeTypeName> translations) {
		this.translations = translations;
	}

	/**
	 * Perform attribute validation
	 *
	 * @param value Attribute value to validate
	 * @throws FlexPayException if validation fails
	 */
	abstract void validate(String value) throws FlexPayException;
}
