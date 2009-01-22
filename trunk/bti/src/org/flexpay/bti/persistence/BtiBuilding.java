package org.flexpay.bti.persistence;

import org.flexpay.ab.persistence.Building;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.Collections;

public class BtiBuilding extends Building {

	private Set<BuildingTempAttribute> attributes = Collections.emptySet();

	public BtiBuilding() {
	}

	public BtiBuilding(@NotNull Long id) {
		super(id);
	}

	public BtiBuilding(@NotNull Stub<BtiBuilding> stub) {
		super(stub.getId());
	}

	public Set<BuildingTempAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<BuildingTempAttribute> attributes) {
		this.attributes = attributes;
	}
}
