package org.flexpay.accounting.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.flexpay.common.persistence.Stub;
import org.flexpay.accounting.persistence.DocumentType;
import org.springframework.security.annotation.Secured;

public interface DocumentTypeService {

	/**
	 * Read DocumentType object by Stub
	 *
	 * @param typeStub document type stub
	 * @return DocumentType object
	 */
	@Secured (Roles.DOCUMENT_TYPE_READ)
	@Nullable
	public DocumentType read(@NotNull Stub<DocumentType> typeStub);

	/**
	 * Save type
	 *
	 * @param documentType DocumentType Object
	 */
	@Secured ({Roles.DOCUMENT_TYPE_ADD, Roles.DOCUMENT_TYPE_CHANGE})
	public void save(@NotNull DocumentType documentType);

	/**
	 * Delete DocumentType object
	 *
	 * @param typeStub type stub
	 */
	@Secured (Roles.DOCUMENT_TYPE_DELETE)
	public void delete(@NotNull Stub<DocumentType> typeStub);

}
