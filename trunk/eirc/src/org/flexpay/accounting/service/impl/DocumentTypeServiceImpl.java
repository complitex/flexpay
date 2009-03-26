package org.flexpay.accounting.service.impl;

import org.flexpay.accounting.dao.DocumentTypeDao;
import org.flexpay.accounting.persistence.DocumentType;
import org.flexpay.accounting.service.DocumentTypeService;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class DocumentTypeServiceImpl implements DocumentTypeService {

	@NonNls
	private Logger log = LoggerFactory.getLogger(getClass());

	private DocumentTypeDao documentTypeDao;

	/**
	 * Read DocumentType object by Stub
	 *
	 * @param typeStub document type stub
	 * @return DocumentType object
	 */
	@Nullable
	public DocumentType read(@NotNull Stub<DocumentType> typeStub) {
		return documentTypeDao.readFull(typeStub.getId());
	}

	/**
	 * Save document type
	 *
	 * @param documentType DocumentType Object
	 */
	@Transactional(readOnly = false)
	public void save(@NotNull DocumentType documentType) {
		if (documentType.isNew()) {
			documentType.setId(null);
			documentTypeDao.create(documentType);
		} else {
			documentTypeDao.update(documentType);
		}
	}

	/**
	 * Delete DocumentType object
	 *
	 * @param typeStub type stub
	 */
	@Transactional(readOnly = false)
	public void delete(@NotNull Stub<DocumentType> typeStub) {
		DocumentType documentType = documentTypeDao.read(typeStub.getId());

		if (documentType == null) {
			log.debug("Can't find document type with id {}", typeStub.getId());
			return;
		}

		documentTypeDao.delete(documentType);
	}

	@Required
	public void setDocumentTypeDao(DocumentTypeDao documentTypeDao) {
		this.documentTypeDao = documentTypeDao;
	}

}
