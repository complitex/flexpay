package org.flexpay.common.dao;

import org.flexpay.common.persistence.MeasureUnit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MeasureUnitDao extends GenericDao<MeasureUnit, Long> {

	/**
	 * Get a list of available measure units
	 *
	 * @return List of Measure units
	 */
	@NotNull
	List<MeasureUnit> listUnits();
}
