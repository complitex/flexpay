package org.flexpay.common.dao.registry;

import org.flexpay.common.persistence.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public interface RegistryDaoExt {

	/**
	 * Find registries by identifiers
	 *
	 * @param objectIds Set of registry identifiers
	 * @return collection of registries
	 */
	Collection<Registry> findRegistries(@NotNull Set<Long> objectIds);

	/**
	 * Check if registry has more records to process
	 *
	 * @param registryId Registry id
	 * @return <code>true</code> if registry has records for processing, or <code>false</code> otherwise
	 */
	boolean hasMoreRecordsToProcess(Long registryId);
}
