package org.flexpay.sz.service;

import java.util.List;

import org.flexpay.sz.persistence.SzFileType;

public interface SzFileTypeService {
	/**
	 * Read SzFileType object by its unique id
	 * 
	 * @param id SzFileType key
	 *            
	 * @return SzFileType object, or <code>null</code> if object not found
	 */
	SzFileType read(Long id);

	/**
	 * Get a list of available SzFileTypes
	 * 
	 * @return List of SzFileType
	 */
	List<SzFileType> getEntities();
	
	/**
	 * Get SzFileTypes object by file name
	 * 
	 * @param fileName file name
	 * 
	 * @return SzFileType
	 */
	SzFileType getByFileName(String fileName);

}
