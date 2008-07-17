package org.flexpay.sz.service;

import java.util.List;

import org.flexpay.sz.persistence.SzFileType;

public interface SzFileTypeService {
	Long TARIF = (long) 1;
	Long CHARACTERISTIC = (long) 2;
	Long SRV_TYPES = (long) 3;
	Long FORM2 = (long) 4;
	Long CHARACTERISTIC_RESPONSE = (long) 5;
	Long SRV_TYPES_RESPONSE = (long) 6;
	Long SUBSIDY = (long) 7;
	
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
