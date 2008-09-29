package org.flexpay.common.persistence;

import java.util.Set;
import java.util.Collections;

public class MeasureUnit extends DomainObjectWithStatus {

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
