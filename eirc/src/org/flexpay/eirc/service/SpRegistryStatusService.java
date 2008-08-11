package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.RegistryStatus;

public interface SpRegistryStatusService {

	/**
	 * Read SpRegistryStatus object by its unique code
	 * 
	 * @param code
	 *            SpRegistryStatus code
	 * @return SpRegistryStatus object, or <code>null</code> if object
	 *         not found
	 */
	RegistryStatus findByCode(int code);
}
