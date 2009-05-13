package org.flexpay.payments.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.math.BigDecimal;
import java.util.List;

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

	/**
	 * Returns list of operation documents which suits search criterias
	 * @param operation owner operation
	 * @param serviceTypeId documnent service type id
	 * @param minimalSumm minimal document summ
	 * @param maximalSumm maximal document summ
	 * @return list of operation documents which suits search criterias
	 */
	@Secured (Roles.DOCUMENT_READ)
	List<Document> searchDocuments(@NotNull Operation operation, Long serviceTypeId, BigDecimal minimalSumm, BigDecimal maximalSumm);
}
