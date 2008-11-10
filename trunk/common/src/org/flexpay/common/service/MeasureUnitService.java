package org.flexpay.common.service;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.MeasureUnitFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Service helping working with MeasureUnit-s
 */
public interface MeasureUnitService {

	/**
	 * Read full unit info
	 *
	 * @param stub MeasureUnit stub to read
	 * @return MeasureUnit if available
	 */
	@Nullable
	MeasureUnit read(@NotNull Stub<MeasureUnit> stub);

	/**
	 * Get a list of available measure units
	 *
	 * @return List of Measure units
	 */
	@NotNull
	List<MeasureUnit> listUnits();

	/**
	 * Initialize filter
	 *
	 * @param filter MeasureUnitFilter to init
	 * @return Filter back, or a new instance if filter is <code>null</code>
	 */
	@NotNull
	MeasureUnitFilter initFilter(@Nullable MeasureUnitFilter filter);

	/**
	 * Create or update measure unit
	 *
	 * @param unit MeasureUnit to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	void save(@NotNull MeasureUnit unit) throws FlexPayExceptionContainer;
}
