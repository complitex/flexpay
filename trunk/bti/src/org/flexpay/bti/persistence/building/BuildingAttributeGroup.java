package org.flexpay.bti.persistence.building;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

/**
 * Group of a building attribute types
 */
public class BuildingAttributeGroup extends DomainObjectWithStatus {

	private Set<BuildingAttributeGroupName> translations = set();

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

	public Set<BuildingAttributeGroupName> getTranslations() {
		return translations;
	}

	@SuppressWarnings ({"UnusedDeclaration"})
	private void setTranslations(Set<BuildingAttributeGroupName> translations) {
		this.translations = translations;
	}

	public void setTranslation(BuildingAttributeGroupName name) {
		translations = TranslationUtil.setTranslation(translations, this, name);
	}

	/**
	 * Get name translation in a specified language
	 *
	 * @param lang Language to get translation in
	 * @return translation if found, or <code>null</code> otherwise
	 */
	@Nullable
	public BuildingAttributeGroupName getTranslation(@NotNull Language lang) {

		for (BuildingAttributeGroupName translation : getTranslations()) {
			if (lang.equals(translation.getLang())) {
				return translation;
			}
		}

		return null;
	}
}
