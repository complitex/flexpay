package org.flexpay.bti.persistence;

import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.Collections;

public class BtiBuilding extends Building {

	private Set<BuildingAttributeBase> attributes = Collections.emptySet();

	public BtiBuilding() {
	}

	public BtiBuilding(@NotNull Long id) {
		super(id);
	}

	public BtiBuilding(@NotNull Stub<BtiBuilding> stub) {
		super(stub.getId());
	}

	public Set<BuildingAttributeBase> getAttributes() {
		return attributes;
	}

	@SuppressWarnings ({"UnusedDeclaration"})
	private void setAttributes(Set<BuildingAttributeBase> attributes) {
		this.attributes = attributes;
	}

	public void addAttribute(BuildingAttributeBase attribute) {
		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (attributes == Collections.EMPTY_SET) {
			attributes = CollectionUtils.set();
		}

		attribute.setBuilding(this);
		attributes.add(attribute);
	}
}
