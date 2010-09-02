package org.flexpay.common.service;

import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.filter.MeasureUnitFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Service interface for MeasureUnits related tasks
 */
public interface MeasureUnitService extends DomainObjectService<MeasureUnit> {

	/**
	 * Get a list of available measure units
	 *
	 * @return List of Measure units
	 */
	@NotNull
	List<MeasureUnit> find();

	/**
	 * Initialize filter
	 *
	 * @param measureUnitFilter filter to init
	 * @return Filter back, or a new instance if filter is <code>null</code>
	 */
	@NotNull
	MeasureUnitFilter initFilter(@Nullable MeasureUnitFilter measureUnitFilter);

	void delete(@NotNull MeasureUnit unit);

}
