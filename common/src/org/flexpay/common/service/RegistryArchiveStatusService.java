package org.flexpay.common.service;

import org.flexpay.common.persistence.registry.RegistryArchiveStatus;

public interface RegistryArchiveStatusService {

	/**
	 * Read RegistryArchiveStatus object by its unique id
	 *
	 * @param id RegistryArchiveStatus key
	 * @return RegistryArchiveStatus object, or <code>null</code> if object not found
	 */
	RegistryArchiveStatus read(Long id);

	/**
	 * Read SpRegistryArchiveStatus object by its unique code
	 *
	 * @param code RegistryArchiveStatus code
	 * @return RegistryArchiveStatus object, or <code>null</code> if object not found
	 */
	RegistryArchiveStatus findByCode(int code);
}
