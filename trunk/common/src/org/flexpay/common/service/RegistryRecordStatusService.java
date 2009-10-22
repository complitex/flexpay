package org.flexpay.common.service;

import org.flexpay.common.persistence.registry.RegistryRecordStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface RegistryRecordStatusService {

	/**
	 * Read RegistryStatus object by its unique code
	 *
	 * @param code RegistryStatus code
	 * @return RegistryStatus object, or <code>null</code> if object
	 *         not found
	 */
	@Nullable
	RegistryRecordStatus findByCode(int code);

	/**
	 * Find all registry statuses
	 * 
	 * @return list of statuses
	 */
	@NotNull
	List<RegistryRecordStatus> listAllStatuses();
}
