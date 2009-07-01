package org.flexpay.payments.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.Organization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.math.BigDecimal;
import java.util.Date;
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
	 *
	 * @param operation owner operation
	 * @param serviceTypeId documnent service type id
	 * @param minimalSumm minimal document summ
	 * @param maximalSumm maximal document summ
	 * @return list of operation documents which suits search criterias
	 */
	@Secured (Roles.DOCUMENT_READ)
	List<Document> searchDocuments(@NotNull Operation operation, Long serviceTypeId, BigDecimal minimalSumm, BigDecimal maximalSumm);

	/**
	 * Returns list of documents with state REGISTERED
	 * and type CASH_PAYMENT which were created in time period
	 *
	 * @param begin begin date
	 * @param end end date
	 * @return list of documents with state REGISTERED
	 * 		   and type CASH_PAYMENT which were created in time period
	 */
	@Secured (Roles.DOCUMENT_READ)
	List<Document> listRegisteredPaymentDocuments(@NotNull Date begin, @NotNull Date end);

    /**
	 * Returns list of documents with state REGISTERED
	 * and type CASH_PAYMENT which were created in time period for service provider
	 *
     * @param serviceProvider service provider
	 * @param begin begin date
	 * @param end end date
	 * @return list of documents with state REGISTERED
	 * 		   and type CASH_PAYMENT which were created in time period
	 */
	@Secured (Roles.DOCUMENT_READ)
	List<Document> listRegisteredPaymentDocuments(@NotNull ServiceProvider serviceProvider, @NotNull Organization organization, @NotNull Date begin, @NotNull Date end);

}
