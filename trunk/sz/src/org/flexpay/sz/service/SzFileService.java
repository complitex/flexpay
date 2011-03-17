package org.flexpay.sz.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFileStatus;
import org.flexpay.sz.persistence.SzFile;
import org.springframework.security.access.annotation.Secured;

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
	@Secured({Roles.SZ_FILE_UPLOAD_FILE})
	SzFile create(SzFile szFile) throws FlexPayException;

	@Secured({Roles.SZ_FILE_DELETE})
	void delete(SzFile szFile);

	/**
	 * Read SzFile object by its unique id
	 * 
	 * @param id SzFile key
	 * @return SzFile object, or <code>null</code> if object not found
	 */
	@Secured({Roles.SZ_FILE_READ})
	SzFile read(Long id);

	/**
	 * Read full SzFile object by its unique id
	 * 
	 * @param id SzFile key
	 * @return full SzFile object, or <code>null</code> if object not found
	 */
	@Secured({Roles.SZ_FILE_READ})
	SzFile readFull(Long id);

	/**
	 * Update SzFile
	 * 
	 * @param importFile SzFile to update for
	 * @return Updated ImportFile object
	 * @throws FlexPayException if SzFile object is invalid
	 */
	@Secured({Roles.SZ_FILE_DELETE_FROM_DB, Roles.SZ_FILE_LOAD_TO_DB, Roles.SZ_FILE_LOAD_FROM_DB})
	SzFile update(SzFile importFile) throws FlexPayException;

	@Secured({Roles.SZ_FILE_DELETE_FROM_DB, Roles.SZ_FILE_LOAD_TO_DB, Roles.SZ_FILE_LOAD_FROM_DB})
	void updateStatus(Collection<Long> fileIds, FPFileStatus status) throws FlexPayException;

	@Secured({Roles.SZ_FILE_READ})
	List<SzFile> listSzFiles(Page<SzFile> pager);

	@Secured({Roles.SZ_FILE_READ})
	List<SzFile> listSzFilesByIds(Collection<Long> fileIds);

}
