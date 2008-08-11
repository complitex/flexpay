package org.flexpay.eirc.service;

import org.flexpay.eirc.persistence.RegistryArchiveStatus;

public interface SpRegistryArchiveStatusService {
	/**
	 * Read SpRegistryArchiveStatus object by its unique id
	 * 
	 * @param id
	 *            SpRegistryArchiveStatus key
	 * @return SpRegistryArchiveStatus object, or <code>null</code> if object
	 *         not found
	 */
	RegistryArchiveStatus read(Long id);
	
	/**
	 * Read SpRegistryArchiveStatus object by its unique code
	 * 
	 * @param code
	 *            SpRegistryArchiveStatus code
	 * @return SpRegistryArchiveStatus object, or <code>null</code> if object
	 *         not found
	 */
	RegistryArchiveStatus findByCode(int code);

}
