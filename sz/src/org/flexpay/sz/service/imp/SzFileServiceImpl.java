package org.flexpay.sz.service.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.sz.dao.SzFileDao;
import org.flexpay.sz.persistence.SzFile;
import org.flexpay.sz.service.SzFileService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class SzFileServiceImpl implements SzFileService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private SzFileDao szFileDao;

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

	/**
	 * Get a list of available identity types
	 *
	 * @return List of SzFile
	 */
	public List<SzFile> getEntities() {
		return szFileDao.listSzFiles();
	}

	@Transactional (readOnly = false)
	public void delete(SzFile szFile) {
		szFileDao.delete(szFile);
	}

	public void setSzFileDao(SzFileDao szFileDao) {
		this.szFileDao = szFileDao;
	}

}
