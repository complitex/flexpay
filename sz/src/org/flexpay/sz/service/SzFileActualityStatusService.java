package org.flexpay.sz.service;

import org.flexpay.sz.persistence.SzFileActualityStatus;

public interface SzFileActualityStatusService {
	Long NOT_ACTUALY = new Long(1);
	Long ACTUALY = new Long(2);

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
