package org.flexpay.sz.service;

import org.flexpay.sz.persistence.SzFileActualityStatus;

public interface SzFileActualityStatusService {
	Long IS_NOT_ACTUAL = 1L;
	Long IS_ACTUAL = 2L;

	/**
	 * Read SzFileActualityStatus object by its unique id
	 * 
	 * @param id
	 *            SzFileActualityStatus key
	 * 
	 * @return SzFileActualityStatus object, or <code>null</code> if object
	 *         not found
	 */
	SzFileActualityStatus read(Long id);
}
