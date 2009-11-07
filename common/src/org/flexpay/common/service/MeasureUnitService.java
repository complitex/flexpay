package org.flexpay.common.service;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.MeasureUnitFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Service interface for MeasureUnits related tasks
 */
public interface MeasureUnitService extends DomainObjectService<MeasureUnit> {

	/**
	 * Read measure unit
	 *
	 * @param unitStub MeasureUnit stub
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	@Override
	MeasureUnit readFull(@NotNull Stub<? extends MeasureUnit> unitStub);

	/**
	 * Disable measure units
	 *
	 * @param unitIds IDs of measure units to disable
	 */
	@Override
	void disable(@NotNull Collection<Long> unitIds);

	/**
	 * Create measure unit
	 *
	 * @param unit MeasureUnit to save
	 * @return Saved instance of measure unit
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	@Override
	MeasureUnit create(@NotNull MeasureUnit unit) throws FlexPayExceptionContainer;

	/**
	 * Update or create measure unit
	 *
	 * @param unit MeasureUnit to save
	 * @return Saved instance of measure unit
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	@NotNull
	@Override
	MeasureUnit update(@NotNull MeasureUnit unit) throws FlexPayExceptionContainer;

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
