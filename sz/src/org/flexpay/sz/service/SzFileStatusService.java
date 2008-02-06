package org.flexpay.sz.service;

import org.flexpay.sz.persistence.SzFileStatus;

public interface SzFileStatusService {

	Long IMPORTED = 1L;
	Long MARKED_FOR_PROCESSING = 2L;
	Long PROCESSING = 3L;
	Long PROCESSED = 4L;
	Long PROCESSED_WITH_WARNINGS = 5L;
	Long MARKED_AS_DELETED = 6L;

	/**
	 * Read SzFileStatus object by its unique id
	 *
	 * @param id SzFileStatus key
	 * @return SzFileStatus object, or <code>null</code> if object not found
	 */
	SzFileStatus read(Long id);
}
