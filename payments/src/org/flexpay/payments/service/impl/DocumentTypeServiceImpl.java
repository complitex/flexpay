package org.flexpay.payments.service.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.dao.DocumentTypeDao;
import org.flexpay.payments.persistence.DocumentType;
import org.flexpay.payments.service.DocumentTypeService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class DocumentTypeServiceImpl implements DocumentTypeService {

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
		log.debug("Read type: {}", typeStub);
		return documentTypeDao.readFull(typeStub.getId());
	}

	/**
	 * Read DocumentType object by Stub
	 *
	 * @param code document type code
	 * @return DocumentType object
	 * @throws FlexPayException if lookup fails
	 */
	@NotNull
	public DocumentType read(int code) throws FlexPayException {
		log.debug("Read type: {}", code);
		List<DocumentType> types = documentTypeDao.findByCode(code);
		if (types.isEmpty()) {
			throw new FlexPayException("Type not found #" + code);
		}
		return types.get(0);
	}

	/**
	 * Save document type
	 *
	 * @param documentType DocumentType Object
	 */
	@Transactional (readOnly = false)
	public void create(@NotNull DocumentType documentType) {
		documentType.setId(null);
		documentTypeDao.create(documentType);
	}

	/**
	 * Save document type
	 *
	 * @param documentType DocumentType Object
	 */
	@Transactional (readOnly = false)
	public void update(@NotNull DocumentType documentType) {
		documentTypeDao.update(documentType);
	}

	/**
	 * Delete DocumentType object
	 *
	 * @param typeStub type stub
	 */
	@Transactional (readOnly = false)
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
