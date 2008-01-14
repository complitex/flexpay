package org.flexpay.sz.service;

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

}
