package org.flexpay.orgs.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.Subdivision;
import org.flexpay.orgs.persistence.filters.SubdivisionFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface SubdivisionService {

	/**
	 * Read organization info with subdivisions
	 *
	 * @param stub Organization stub
	 * @return Organization
	 * @throws FlexPayException if stub references invalid object
	 */
	@NotNull
	List<Subdivision> getOrganizationSubdivisions(@NotNull Stub<Organization> stub) throws FlexPayException;

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
	Subdivision read(@NotNull Stub<Subdivision> stub);

	/**
	 * Save subdivision
	 *
	 * @param subdivision Subdivision to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	void create(@NotNull Subdivision subdivision) throws FlexPayExceptionContainer;

	/**
	 * update subdivision
	 *
	 * @param subdivision Subdivision to save
	 * @throws FlexPayExceptionContainer if validation fails
	 */
	void update(@NotNull Subdivision subdivision) throws FlexPayExceptionContainer;

	/**
	 * Initialize subdivision filter
	 *
	 * @param subdivisionFilter Filter to initialize
	 * @param stub			  Organization that departments to put to filter
	 */
	void initFilter(@NotNull SubdivisionFilter subdivisionFilter, @NotNull Stub<Organization> stub);
}
