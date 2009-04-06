package org.flexpay.eirc.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
	 * @param pager	Page
	 * @param minMaxIds Minimum and maximum record ids for processing
	 * @return list of records
	 */
	List<RegistryRecord> getRecordsForProcessing(@NotNull Stub<Registry> registry, Page<RegistryRecord> pager, Long[] minMaxIds);

	/**
	 * Check if RegistryFile was loaded
	 *
	 * @param stub File stub
	 * @return <code>true</code> if file already loaded, or <code>false</code> otherwise
	 */
	boolean isLoaded(@NotNull Stub<FPFile> stub);

}
