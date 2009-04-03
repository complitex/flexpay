package org.flexpay.payments.service.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.dao.DocumentDao;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.service.DocumentService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true)
public class DocumentServiceImpl implements DocumentService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private DocumentDao documentDao;

	/**
	 * Read Document object by Stub
	 *
	 * @param documentStub document stub
	 * @return Document object
	 */
	@Nullable
	public Document read(@NotNull Stub<Document> documentStub) {
		return documentDao.readFull(documentStub.getId());
	}

	/**
	 * Save document
	 *
	 * @param document Document Object
	 */
	@Transactional (readOnly = false)
	public void save(@NotNull Document document) {
		if (document.isNew()) {
			document.setId(null);
			documentDao.create(document);
		} else {
			documentDao.update(document);
		}
	}

	/**
	 * Delete Document object
	 *
	 * @param documentStub document stub
	 */
	@Transactional (readOnly = false)
	public void delete(@NotNull Stub<Document> documentStub) {
		Document document = documentDao.read(documentStub.getId());

		if (document == null) {
			log.debug("Can't find document with id {}", documentStub.getId());
			return;
		}

		documentDao.delete(document);
	}

	@Required
	public void setDocumentDao(DocumentDao documentDao) {
		this.documentDao = documentDao;
	}

}
