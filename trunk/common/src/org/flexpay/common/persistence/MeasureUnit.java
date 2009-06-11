package org.flexpay.common.persistence;

import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class MeasureUnit extends DomainObjectWithStatus {

	public Set<MeasureUnitName> unitNames = Collections.emptySet();

	/**
	 * Constructs a new DomainObject.
	 */
	public MeasureUnit() {
	}

	public MeasureUnit(@NotNull Long id) {
		super(id);
	}

	public MeasureUnit(@NotNull Stub<MeasureUnit> stub) {
		super(stub.getId());
	}

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

	public void setName(MeasureUnitName unitName) {
		unitNames = TranslationUtil.setTranslation(unitNames, this, unitName);
	}
}
