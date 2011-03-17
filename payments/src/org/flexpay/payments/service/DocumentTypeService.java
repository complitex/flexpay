package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.DocumentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;

public interface DocumentTypeService {

	/**
	 * Read DocumentType object by Stub
	 *
	 * @param typeStub document type stub
	 * @return DocumentType object
	 */
	@Secured (Roles.DOCUMENT_TYPE_READ)
	@Nullable
	DocumentType read(@NotNull Stub<DocumentType> typeStub);

	/**
	 * Read DocumentType object by Stub
	 *
	 * @param code document type code
	 * @return DocumentType object
	 * @throws FlexPayException if lookup fails
	 */
	@Secured (Roles.DOCUMENT_TYPE_READ)
	@NotNull
	DocumentType read(int code) throws FlexPayException;

	/**
	 * Save type
	 *
	 * @param documentType DocumentType Object
	 */
	@Secured (Roles.DOCUMENT_TYPE_ADD)
	void create(@NotNull DocumentType documentType);

	/**
	 * Update type
	 *
	 * @param documentType DocumentType Object
	 */
	@Secured (Roles.DOCUMENT_TYPE_CHANGE)
	void update(@NotNull DocumentType documentType);

	/**
	 * Delete DocumentType object
	 *
	 * @param typeStub type stub
	 */
	@Secured (Roles.DOCUMENT_TYPE_DELETE)
	void delete(@NotNull Stub<DocumentType> typeStub);
}
