package org.flexpay.sz.service.imp;

import java.util.List;

import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.sz.dao.ImportFileDao;
import org.flexpay.sz.persistence.ImportFile;
import org.flexpay.sz.service.ImportFileService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ImportFileServiceImpl implements ImportFileService {
	private static Logger log = Logger.getLogger(ImportFileServiceImpl.class);

	private ImportFileDao importFileDao;

	/**
	 * Create ImportFile
	 * 
	 * @param importFile
	 *            ImportFile object
	 * @return created ImportFile object
	 */
	@Transactional(readOnly = false)
	public ImportFile create(ImportFile importFile) throws FlexPayException {
		importFileDao.create(importFile);

		if (log.isDebugEnabled()) {
			log.debug("Created ImportFile: " + importFile);
		}

		return importFile;
	}

	/**
	 * Read ImportFile object by its unique id
	 * 
	 * @param id
	 *            ImportFile key
	 * @return ImportFile object, or <code>null</code> if object not found
	 */
	public ImportFile read(Long id) {
		// TODO realize logic
		return null;
	}

	/**
	 * Update ImportFile
	 * 
	 * @param importFile
	 *            ImportFile to update for
	 * @return Updated ImportFile object
	 * @throws FlexPayException
	 *             if ImportFile object is invalid
	 */
	public ImportFile update(ImportFile importFile) throws FlexPayException {
		// TODO realize logic
		return importFile;

	}
	
	/**
	 * Get a list of available identity types
	 * 
	 * @return List of IdentityType
	 */
	public List<ImportFile> getEntities() {
		return importFileDao.listImportFiles();
	}

	public void setImportFileDao(ImportFileDao importFileDao) {
		this.importFileDao = importFileDao;
	}

}
