package org.flexpay.payments.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

public interface DocumentService {

	/**
	 * Read Document object by Stub
	 *
	 * @param documentStub document stub
	 * @return Document object
	 */
	@Secured (Roles.DOCUMENT_READ)
	@Nullable
	Document read(@NotNull Stub<Document> documentStub);

	/**
	 * Save document
	 *
	 * @param document Document Object
	 */
	@Secured ({Roles.DOCUMENT_ADD, Roles.DOCUMENT_CHANGE})
	void save(@NotNull Document document);

	/**
	 * Delete Document object
	 *
	 * @param documentStub document stub
	 */
	@Secured (Roles.DOCUMENT_DELETE)
	void delete(@NotNull Stub<Document> documentStub);

}
