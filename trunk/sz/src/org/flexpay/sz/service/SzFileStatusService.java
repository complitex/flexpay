package org.flexpay.sz.service;

import org.flexpay.sz.persistence.SzFileStatus;

public interface SzFileStatusService {
	
	Long IMPORTED = new Long(1);
	Long MARKED_FOR_PROCESSING = new Long(2);
	Long PROCESSING = new Long(3);
	Long PROCESSED = new Long(4);
	Long PROCESSED_WITH_WARNINGS = new Long(5);
	Long MARKED_AS_DELETED = new Long(6);
	
	/**
	 * Read SzFileStatus object by its unique id
	 * 
	 * @param id SzFileStatus key
	 *            
	 * @return SzFileStatus object, or <code>null</code> if object not found
	 */
	SzFileStatus read(Long id);
	
	

}
