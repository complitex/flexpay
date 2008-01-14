package org.flexpay.sz.service.imp;

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

	public void setImportFileDao(ImportFileDao importFileDao) {
		this.importFileDao = importFileDao;
	}

}
