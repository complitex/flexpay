package org.flexpay.sz.service;

import java.util.List;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.sz.persistence.SzFile;

public interface SzFileService {
	/**
	 * Create SzFile
	 *
	 * @param szFile SzFile
	 * @return created SzFile object
	 */
	public SzFile create(SzFile szFile) throws FlexPayException;
	
	/**
	 * Read SzFile object by its unique id
	 *
	 * @param id SzFile key
	 * @return SzFile object, or <code>null</code> if object not found
	 */
	SzFile read(Long id);
	
	/**
	 * Read full SzFile object by its unique id
	 *
	 * @param id SzFile key
	 * @return full SzFile object, or <code>null</code> if object not found
	 */
	SzFile readFull(Long id);
	
	/**
	 * Update SzFile
	 *
	 * @param szFile SzFile to update for
	 * @return Updated ImportFile object
	 * @throws FlexPayException if SzFile object is invalid
	 */
	SzFile update(SzFile importFile)
			throws FlexPayException;
	
	/**
	 * Get a list of available SzFile
	 * 
	 * @return List of SzFile
	 */
	List<SzFile> getEntities();

}
