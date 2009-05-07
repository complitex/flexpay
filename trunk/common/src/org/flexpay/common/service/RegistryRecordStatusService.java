package org.flexpay.common.service;

import org.flexpay.common.persistence.registry.RegistryRecordStatus;

public interface RegistryRecordStatusService {

	/**
	 * Read SpRegistryStatus object by its unique code
	 *
	 * @param code SpRegistryStatus code
	 * @return SpRegistryStatus object, or <code>null</code> if object
	 *         not found
	 */
	RegistryRecordStatus findByCode(int code);

}
