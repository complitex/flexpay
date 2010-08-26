package org.flexpay.common.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface RegistryService {

	/**
	 * Create Registry
	 *
	 * @param registry Registry
	 * @return created Registry object
	 * @throws FlexPayException if failure occurs
	 */
	Registry create(Registry registry) throws FlexPayException;

	/**
	 * Get all Registry by File id in page mode
	 *
	 * @param pager	Page object
	 * @param fileId File id
	 * @return List of Registry objects for pager
	 */
	List<Registry> findObjects(Page<Registry> pager, Long fileId);

	/**
	 * Read Registry object by its unique id
	 *
	 * @param stub Registry stub
	 * @return Registry object, or <code>null</code> if object not found
	 */
	@Nullable
	Registry read(@NotNull Stub<Registry> stub);

	/**
	 * Read Registry with containers included
	 *
	 * @param stub Registry stub
	 * @return Registry if found, or <code>null</code> otherwise
	 */
	@Nullable
	Registry readWithContainers(@NotNull Stub<Registry> stub);

	/**
	 * Update Registry
	 *
	 * @param registry Registry to update for
	 * @return Updated Registry object
	 * @throws FlexPayException if Registry object is invalid
	 */
	Registry update(Registry registry) throws FlexPayException;

	void delete(Registry registry);

	/**
	 * Find registries by identifiers
	 *
	 * @param objectIds Set of registry identifiers
	 * @return collection of registries
	 */
	Collection<Registry> findObjects(@NotNull Set<Long> objectIds);

    List<Registry> findRegistries(int typeCode, Date from, Date till);

    List<Registry> findRegistriesInDateInterval(Date from, Date till);

	/**
	 * Delete all records for registry
	 *
	 * @param stub registry stub
	 */
	void deleteRecords(Stub<Registry> stub);

	/**
	 * Returns number of registries which corresponds following parameters
     *
	 * @param typeCode registry type code
	 * @param recipientCode recipient organization id
	 * @param from lower bound for createion date
	 * @param till higher bound for creation date
	 * @return number of registries which corresponds following parameters
	 */
	Long getRegistriesCount(int typeCode, Long recipientCode, Date from, Date till);

	/**
	 * Return number of registry errors and update registry in field {@link org.flexpay.common.persistence.registry.Registry#errorsNumber}
     *
	 * @param registry Registry
	 * @return number of registry errors
	 */
	int checkRegistryErrorsNumber(@NotNull Registry registry);
}
