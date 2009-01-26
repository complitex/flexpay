package org.flexpay.sz.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.FPFileStatus;
import org.flexpay.sz.persistence.SzFile;

import java.util.Collection;
import java.util.List;

public interface SzFileService {
	/**
	 * Create SzFile
	 * 
	 * @param szFile SzFile
	 * @return created SzFile object
	 * @throws org.flexpay.common.exception.FlexPayException if has error
	 */
	SzFile create(SzFile szFile) throws FlexPayException;

	void delete(SzFile szFile);

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
	 * @param importFile SzFile to update for
	 * @return Updated ImportFile object
	 * @throws FlexPayException if SzFile object is invalid
	 */
	SzFile update(SzFile importFile) throws FlexPayException;

	void updateStatus(Collection<Long> fileIds, FPFileStatus status) throws FlexPayException;

	List<SzFile> listSzFiles(Page<SzFile> pager);

	List<SzFile> listSzFilesByIds(Collection<Long> fileIds);

}
