package org.flexpay.bti.persistence.apartment;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * Group of a apartment attribute types
 */
public class ApartmentAttributeGroup extends DomainObjectWithStatus {

	public ApartmentAttributeGroup() {
	}

	public ApartmentAttributeGroup(@NotNull Long id) {
		super(id);
	}

	public ApartmentAttributeGroup(Stub<ApartmentAttributeGroup> stub) {
		super(stub.getId());
	}

	private Set<ApartmentAttributeGroupName> translations = Collections.emptySet();

	public Set<ApartmentAttributeGroupName> getTranslations() {
		return translations;
	}

	@SuppressWarnings ({"UnusedDeclaration"})
	private void setTranslations(Set<ApartmentAttributeGroupName> translations) {
		this.translations = translations;
	}

	public void addTranslation(ApartmentAttributeGroupName name) {
		translations = TranslationUtil.setTranslation(translations, this, name);
	}

}
