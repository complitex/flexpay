package org.flexpay.payments.service.impl;

import org.flexpay.payments.service.DocumentStatusService;
import org.flexpay.payments.persistence.DocumentStatus;
import org.flexpay.payments.dao.DocumentStatusDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.exception.FlexPayException;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

@Transactional (readOnly = true)
public class DocumentStatusServiceImpl implements DocumentStatusService {

	private DocumentStatusDao documentStatusDao;

	/**
	 * Read DocumentStatus object by Stub
	 *
	 * @param stub document status stub
	 * @return DocumentStatus object if found, or <code>null</code> otherwise
	 */
	public DocumentStatus read(@NotNull Stub<DocumentStatus> stub) {
		return documentStatusDao.readFull(stub.getId());
	}

	/**
	 * Read DocumentStatus object by Stub
	 *
	 * @param code document status code
	 * @return DocumentStatus object
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if code is invalid
	 */
	@NotNull
	public DocumentStatus read(int code) throws FlexPayException {

		List<DocumentStatus> statuses = documentStatusDao.findByCode(code);
		if (statuses.isEmpty()) {
			throw new FlexPayException("Status not found #" + code);
		}
		return statuses.get(0);
	}

	@Required
	public void setDocumentStatusDao(DocumentStatusDao documentStatusDao) {
		this.documentStatusDao = documentStatusDao;
	}
}
