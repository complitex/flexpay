package org.flexpay.accounting.service;

import org.jetbrains.annotations.NotNull;
import org.flexpay.common.persistence.Stub;
import org.flexpay.accounting.persistence.DocumentType;

public interface DocumentTypeService {

	/**
	 * Read DocumentType object by Stub
	 *
	 * @param typeStub document type stub
	 * @return DocumentType object
	 */
	public DocumentType read(@NotNull Stub<DocumentType> typeStub);

	/**
	 * Save type
	 *
	 * @param documentType DocumentType Object
	 */
	public void save(@NotNull DocumentType documentType);

	/**
	 * Delete DocumentType object
	 *
	 * @param typeStub type stub
	 */
	public void delete(@NotNull Stub<DocumentType> typeStub);

}
