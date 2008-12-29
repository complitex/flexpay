package org.flexpay.common.persistence;

import org.jetbrains.annotations.NotNull;
import org.flexpay.common.util.TranslationUtil;

import java.util.Set;
import java.util.Collections;

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
