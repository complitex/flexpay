package org.flexpay.sz.service.imp;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFileStatus;
import org.flexpay.sz.dao.SzFileDao;
import org.flexpay.sz.dao.SzFileDaoExt;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class SzFileServiceImpl implements SzFileService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private SzFileDao szFileDao;
	private SzFileDaoExt szFileDaoExt;

	/**
	 * Create SzFile
	 *
	 * @param importFile SzFile object
	 * @return created ImportFile object
	 */
	@Transactional (readOnly = false)
	public SzFile create(SzFile importFile) throws FlexPayException {
		szFileDao.create(importFile);
		log.debug("Created ImportFile: {}", importFile);
		return importFile;
	}

	@Transactional (readOnly = false)
	public void delete(SzFile szFile) {
		szFileDao.delete(szFile);
	}

	/**
	 * Read SzFile object by its unique id
	 *
	 * @param id SzFile key
	 * @return SzFile object, or <code>null</code> if object not found
	 */
	public SzFile read(Long id) {
		return szFileDao.read(id);
	}

	/**
	 * Read full SzFile object by its unique id
	 *
	 * @param id SzFile key
	 * @return full SzFile object, or <code>null</code> if object not found
	 */
	public SzFile readFull(Long id) {
		return szFileDao.readFull(id);
	}

	/**
	 * Update SzFile
	 *
	 * @param importFile SzFile to update for
	 * @return Updated SzFile object
	 * @throws FlexPayException if SzFile object is invalid
	 */
	@Transactional (readOnly = false)
	public SzFile update(SzFile importFile) throws FlexPayException {
		szFileDao.update(importFile);
		return importFile;
	}

	@Transactional (readOnly = false)
	public void updateStatus(Collection<Long> fileIds, FPFileStatus status) throws FlexPayException {
		szFileDaoExt.updateStatus(fileIds, status);
	}

	public List<SzFile> listSzFiles(Page<SzFile> pager) {
		return szFileDao.findSzFiles(pager);
	}

	public List<SzFile> listSzFilesByIds(Collection<Long> fileIds) {
		return szFileDao.findSzFilesByIds(fileIds);
	}

	@Required
	public void setSzFileDao(SzFileDao szFileDao) {
		this.szFileDao = szFileDao;
	}

	@Required
	public void setSzFileDaoExt(SzFileDaoExt szFileDaoExt) {
		this.szFileDaoExt = szFileDaoExt;
	}

}
