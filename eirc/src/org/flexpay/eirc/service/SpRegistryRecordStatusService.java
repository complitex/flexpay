package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.RegistryRecordStatus;

public interface SpRegistryRecordStatusService {

	/**
	 * Read SpRegistryStatus object by its unique code
	 *
	 * @param code SpRegistryStatus code
	 * @return SpRegistryStatus object, or <code>null</code> if object
	 *         not found
	 */
	RegistryRecordStatus findByCode(int code);
}
