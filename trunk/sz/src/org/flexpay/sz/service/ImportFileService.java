package org.flexpay.sz.service;

import java.util.List;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.sz.persistence.ImportFile;

public interface ImportFileService {
	/**
	 * Create ImportFile
	 *
	 * @param importFile ImportFile
	 * @return created ImportFile object
	 */
	public ImportFile create(ImportFile importFile) throws FlexPayException;
	
	/**
	 * Read ImportFile object by its unique id
	 *
	 * @param id ImportFile key
	 * @return ImportFile object, or <code>null</code> if object not found
	 */
	ImportFile read(Long id);
	
	/**
	 * Update ImportFile
	 *
	 * @param importFile ImportFile to update for
	 * @return Updated ImportFile object
	 * @throws FlexPayException if ImportFile object is invalid
	 */
	ImportFile update(ImportFile importFile)
			throws FlexPayException;
	
	/**
	 * Get a list of available identity types
	 * 
	 * @return List of IdentityType
	 */
	List<ImportFile> getEntities();

}
