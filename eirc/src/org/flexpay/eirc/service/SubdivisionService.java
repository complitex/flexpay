package org.flexpay.eirc.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.Organisation;
import org.flexpay.eirc.persistence.Subdivision;
import org.flexpay.eirc.persistence.filters.SubdivisionFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.List;

public interface SubdivisionService {

	/**
	 * Read organisation info with subdivisions
	 *
	 * @param stub Organisation stub
	 * @return Organisation
	 * @throws FlexPayException if stub references invalid object
	 */
	@NotNull
	List<Subdivision> getOrganisationSubdivisions(@NotNull Stub<Organisation> stub) throws FlexPayException;

	/**
	 * Disable subdivisions
	 *
	 * @param objectIds Subdivision identifiers to disable
	 */
	void disable(@NotNull Set<Long> objectIds);

	/**
	 * Read full Subdivision info
	 *
	 * @param stub Subdivision stub
	 * @return Bank
	 */
	@Nullable
	Subdivision read(@NotNull Subdivision stub);

	/**
	 * Save or update subdivision
	 *
	 * @param subdivision Subdivision to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	void save(@NotNull Subdivision subdivision) throws FlexPayExceptionContainer;

	/**
	 * Initialize subdivision filter
	 *
	 * @param subdivisionFilter Filter to initialize
	 * @param stub Organisation that departments to put to filter
	 */
	void initFilter(@NotNull SubdivisionFilter subdivisionFilter, @NotNull Stub<Organisation> stub);
}
