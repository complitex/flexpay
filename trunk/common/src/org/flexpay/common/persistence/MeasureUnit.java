package org.flexpay.common.persistence;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.Collections;

public class MeasureUnit extends DomainObjectWithStatus {

	/**
	 * Constructs a new DomainObject.
	 */
	public MeasureUnit() {
	}

	public MeasureUnit(@NotNull Long id) {
		super(id);
	}

	public Set<MeasureUnitName> unitNames = Collections.emptySet();

	public Set<MeasureUnitName> getUnitNames() {
		return unitNames;
	}

	public void setUnitNames(Set<MeasureUnitName> unitNames) {
		this.unitNames = unitNames;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof MeasureUnit && super.equals(o);
	}
}
