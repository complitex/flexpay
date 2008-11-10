package org.flexpay.common.persistence.filter;

import org.flexpay.common.persistence.MeasureUnit;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class MeasureUnitFilter extends PrimaryKeyFilter<MeasureUnit> {

	private List<MeasureUnit> measureUnits = Collections.emptyList();

	public List<MeasureUnit> getMeasureUnits() {
		return measureUnits;
	}

	public void setMeasureUnits(List<MeasureUnit> measureUnits) {
		this.measureUnits = measureUnits;
	}

	@Nullable
	public MeasureUnit getSelected() {

		Long selectedId = getSelectedId();
		if (needFilter() && selectedId != null) {
			for (MeasureUnit unit : measureUnits) {
				if (selectedId.equals(unit.getId())) {
					return unit;
				}
			}
		}

		return null;
	}
}
