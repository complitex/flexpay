package org.flexpay.sz.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFileStatus;
import org.flexpay.sz.persistence.SzFile;

import java.util.Collection;
import java.util.List;

public interface SzFileService {
	/**
	 * Create SzFile
	 * 
	 * @param szFile
	 *            SzFile
	 * @return created SzFile object
	 */
	SzFile create(SzFile szFile) throws FlexPayException;

	/**
	 * Read SzFile object by its unique id
	 * 
	 * @param id
	 *            SzFile key
	 * @return SzFile object, or <code>null</code> if object not found
	 */
	SzFile read(Long id);

	/**
	 * Read full SzFile object by its unique id
	 * 
	 * @param id
	 *            SzFile key
	 * @return full SzFile object, or <code>null</code> if object not found
	 */
	SzFile readFull(Long id);

	/**
	 * Update SzFile
	 * 
	 * @param importFile
	 *            SzFile to update for
	 * @return Updated ImportFile object
	 * @throws FlexPayException
	 *             if SzFile object is invalid
	 */
	SzFile update(SzFile importFile) throws FlexPayException;

	void updateStatus(Collection<Long> fileIds, FPFileStatus status) throws FlexPayException;

	/**
	 * Get a list of available SzFile
	 * 
	 * @return List of SzFile
	 */
	List<SzFile> getEntities();

	void delete(SzFile szFile);

}
