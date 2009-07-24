package org.flexpay.payments.service;

import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
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
	@Secured (Roles.DOCUMENT_ADD)
	void create(@NotNull Document document);

	/**
	 * Save document
	 *
	 * @param document Document Object
	 */
	@Secured (Roles.DOCUMENT_CHANGE)
	void update(@NotNull Document document);

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

	/**
	 * Returns summ of payments for service in the cashbox for the period
	 * @param cashboxStub cashbox stub
	 * @param statusCode payment status code
	 * @param serviceTypeCode service type code
	 * @param beginDate begin date
	 * @param endDate end date
	 * @return summ of payments for service in the cashbox
	 */
	BigDecimal getCashboxServiceSumm(Stub<Cashbox> cashboxStub, int statusCode, int serviceTypeCode, Date beginDate, Date endDate);

	/**
	 * Returns summ of payments in the cashbox for the period
	 * @param cashboxStub cashbox stub
	 * @param statusCode payment status code
	 * @param beginDate begin date
	 * @param endDate end date
	 * @return summ of payments in the cashbox
	 */
	BigDecimal getCashboxTotalSumm(Stub<Cashbox> cashboxStub, int statusCode, Date beginDate, Date endDate);

	/**
	 * Returns summ of service payments in the payment point for the period
	 * @param paymentPointStub payment point stub
	 * @param statusCode payment status code
	 * @param serviceTypeCode service type code
	 * @param beginDate begin date
	 * @param endDate end date
	 * @return summ of service payments in the payment point for the period
	 */
	BigDecimal getPaymentPointServiceSumm(Stub<PaymentPoint> paymentPointStub, int statusCode, int serviceTypeCode, Date beginDate, Date endDate);

	/**
	 * Returns total summ of payments in cashbox for the period
	 * @param paymentPointStub payment point stub
	 * @param statusCode payment status code
	 * @param beginDate begin date
	 * @param endDate end date
	 * @return total summ of payments in cashbox for the period
	 */
	BigDecimal getPaymentPointTotalSumm(Stub<PaymentPoint> paymentPointStub, int statusCode, Date beginDate, Date endDate);

	/**
	 * Returns summ of service payment in operation
	 * @param operationStub operation stub
	 * @param serviceTypeCode service type code
	 * @return summ of service payment in operation
	 */
	BigDecimal getOperationServiceSumm(Stub<Operation> operationStub, int serviceTypeCode);
}
