package org.flexpay.bti.persistence.apartment;

import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

/**
 * Group of a apartment attribute types
 */
public class ApartmentAttributeGroup extends DomainObjectWithStatus {

    private Set<ApartmentAttributeGroupName> translations = set();

	public ApartmentAttributeGroup() {
	}

	public ApartmentAttributeGroup(@NotNull Long id) {
		super(id);
	}

	public ApartmentAttributeGroup(Stub<ApartmentAttributeGroup> stub) {
		super(stub.getId());
	}

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
