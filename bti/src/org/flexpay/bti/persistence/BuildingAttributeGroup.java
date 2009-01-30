package org.flexpay.bti.persistence;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.Collections;

/**
 * Group of a building attribute types
 */
public class BuildingAttributeGroup extends DomainObjectWithStatus {

	/**
	 * Constructs a new DomainObject.
	 */
	public BuildingAttributeGroup() {
	}

	public BuildingAttributeGroup(@NotNull Long id) {
		super(id);
	}

	public BuildingAttributeGroup(Stub<BuildingAttributeGroup> stub) {
		super(stub.getId());
	}

	private Set<BuildingAttributeGroupName> translations = Collections.emptySet();

	public Set<BuildingAttributeGroupName> getTranslations() {
		return translations;
	}

	@SuppressWarnings ({"UnusedDeclaration"})
	private void setTranslations(Set<BuildingAttributeGroupName> translations) {
		this.translations = translations;
	}

	public void addTranslation(BuildingAttributeGroupName name) {
		translations = TranslationUtil.setTranslation(translations, this, name);
	}
}
