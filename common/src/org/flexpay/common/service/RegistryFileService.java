package org.flexpay.common.service;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public interface RegistryFileService {

	/**
	 * Get registries for file
	 *
	 * @param spFile ServiceProvider obtained file
	 * @return List of registries in a file
	 */
	List<Registry> getRegistries(FPFile spFile);

	/**
	 * Get registry records for processing
	 *
	 * @param registry Registry header stub
	 * @param range	FetchRange
	 * @return list of records
	 */
	List<RegistryRecord> getRecordsForProcessing(@NotNull Stub<Registry> registry, FetchRange range);

	/**
	 * Get registry records for processing with status LOADED and FIXED
	 *
	 * @param registry Registry header
	 * @param range	Fetch range
	 * @return list of records
	 */
	List<RegistryRecord> getLoadedAndFixedRecords(@NotNull Stub<Registry> registry, FetchRange range);

	/**
	 * Get registry records for processing
	 *
	 * @param recordIds Registry records id
	 * @return list of records
	 */
	List<RegistryRecord> getRecordsForProcessing(Set<Long> recordIds);

	/**
	 * Get fetch range records for processing if was processing some records
	 *
	 * @param registry Processing registry
	 * @param pageSize Page size
	 * @param lastProcessedRegistryRecord Last processed registry record
	 * @return Fetch range
	 */
	FetchRange getFetchRangeForProcessing(@NotNull Stub<Registry> registry, int pageSize, @NotNull Long lastProcessedRegistryRecord);

	/**
	 * Check if RegistryFile was loaded
	 *
	 * @param stub File stub
	 * @return <code>true</code> if file already loaded, or <code>false</code> otherwise
	 */
	boolean isLoaded(@NotNull Stub<FPFile> stub);

	/**
	 * Check exist registry records with status FIXED and LOADED
	 *
	 * @param registry Registry stub
	 * @return <code>true</code> if records exist, or <code>false</code> otherwise
	 */
	boolean hasLoadedAndFixedRecords(@NotNull Stub<Registry> registry);

}
