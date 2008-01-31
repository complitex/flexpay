package org.flexpay.sz.service;

import java.util.List;

import org.flexpay.sz.persistence.SzFileType;

public interface SzFileTypeService {
	Long TARIF = new Long(1);
	Long CHARACTERISTIC = new Long(2);
	Long SRV_TYPES = new Long(3);
	Long FORM2 = new Long(4);
	Long CHARACTERISTIC_RESPONSE = new Long(5);
	Long SRV_TYPES_RESPONSE = new Long(6);
	Long SUBSIDY = new Long(7);
	
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
