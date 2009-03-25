package org.flexpay.accounting.service;

import org.flexpay.accounting.persistence.Document;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

public interface DocumentService {

	/**
	 * Read Document object by Stub
	 *
	 * @param documentStub document stub
	 * @return Document object
	 */
	public Document read(@NotNull Stub<Document> documentStub);

	/**
	 * Save document
	 *
	 * @param document Document Object
	 */
	public void save(@NotNull Document document);

	/**
	 * Delete Document object
	 *
	 * @param documentStub document stub
	 */
	public void delete(@NotNull Stub<Document> documentStub);

}
