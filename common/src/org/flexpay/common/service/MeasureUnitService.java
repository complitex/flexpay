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
public interface MeasureUnitService extends DomainObjectService<MeasureUnit> {

	/**
	 * Read full unit info
	 *
	 * @param stub MeasureUnit stub to read
	 * @return MeasureUnit if available
	 */
	@Nullable
	MeasureUnit readFull(@NotNull Stub<? extends MeasureUnit> stub);

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
	 * Create measure unit
	 *
	 * @param unit MeasureUnit to save
	 * @return Persisted object back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	MeasureUnit create(@NotNull MeasureUnit unit) throws FlexPayExceptionContainer;

	/**
	 * Update measure unit
	 *
	 * @param obj Unit to update
	 * @return updatyed object back
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	@Override
	MeasureUnit update(@NotNull MeasureUnit obj) throws FlexPayExceptionContainer;

	void delete(@NotNull MeasureUnit unit);
}
