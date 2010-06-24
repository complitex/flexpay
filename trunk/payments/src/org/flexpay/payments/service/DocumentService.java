package org.flexpay.payments.service;

import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.Service;
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
	 * @param operation	 owner operation
	 * @param serviceTypeId documnent service type id
	 * @param minimalSum   minimal document sum
	 * @param maximalSum   maximal document sum
	 * @return list of operation documents which suits search criterias
	 */
	@Secured (Roles.DOCUMENT_READ)
	List<Document> searchDocuments(@NotNull Stub<Operation> operation, Long serviceTypeId, BigDecimal minimalSum, BigDecimal maximalSum);

	/**
	 * Returns list of documents with state REGISTERED and type CASH_PAYMENT which were created in time period
	 *
	 * @param begin begin date
	 * @param end   end date
	 * @return list of documents with state REGISTERED and type CASH_PAYMENT which were created in time period
	 */
	@Secured (Roles.DOCUMENT_READ)
	List<Document> listRegisteredPaymentDocuments(@NotNull Date begin, @NotNull Date end);

	/**
	 * Returns list of documents with state REGISTERED and type CASH_PAYMENT which were created in time period for service
	 * provider
	 *
	 * @param providerStub Service provider stub
	 * @param orgStub	  Organization stub
	 * @param range		DateRange
	 * @return list of documents with state REGISTERED and type CASH_PAYMENT which were created in time period
	 */
	@Secured (Roles.DOCUMENT_READ)
	List<Document> listRegisteredPaymentDocuments(@NotNull Stub<ServiceProvider> providerStub,
												  @NotNull Stub<Organization> orgStub, @NotNull DateRange range);

	/**
	 * Returns list of services for documents with state REGISTERED and type CASH_PAYMENT that was created in a time period
	 * for service provider
	 *
	 * @param providerStub Service provider stub
	 * @param orgStub	  Organization stub
	 * @param range		DateRange
	 * @return Services list
	 */
	@Secured (Roles.SERVICE_READ)
	List<Service> listPaymentsServices(@NotNull Stub<ServiceProvider> providerStub,
									   @NotNull Stub<Organization> orgStub, @NotNull DateRange range);

	/**
	 * Returns list of payment points for documents with state REGISTERED and type CASH_PAYMENT that was created in a time
	 * period for service provider
	 *
	 * @param providerStub Service provider stub
	 * @param orgStub	  Organization stub
	 * @param range		DateRange
	 * @return payment points list
	 */
	@Secured (org.flexpay.orgs.service.Roles.PAYMENT_POINT_READ)
	List<PaymentPoint> listPaymentsPoints(@NotNull Stub<ServiceProvider> providerStub,
										  @NotNull Stub<Organization> orgStub, @NotNull DateRange range);

	/**
	 * Returns sum of payments for service in the cashbox for the period
	 *
	 * @param cashboxStub	 cashbox stub
	 * @param statusCode	  payment status code
	 * @param serviceTypeCode service type code
	 * @param beginDate	   begin date
	 * @param endDate		 end date
	 * @return sum of payments for service in the cashbox
	 */
	BigDecimal getCashboxServiceSumm(Stub<Cashbox> cashboxStub, int statusCode, int serviceTypeCode, Date beginDate, Date endDate);

	/**
	 * Returns sum of payments in the cashbox for the period
	 *
	 * @param cashboxStub cashbox stub
	 * @param statusCode  payment status code
	 * @param beginDate   begin date
	 * @param endDate	 end date
	 * @return sum of payments in the cashbox
	 */
	BigDecimal getCashboxTotalSumm(Stub<Cashbox> cashboxStub, int statusCode, Date beginDate, Date endDate);

	/**
	 * Returns sum of service payments in the payment point for the period
	 *
	 * @param paymentPointStub payment point stub
	 * @param statusCode	   payment status code
	 * @param serviceTypeCode  service type code
	 * @param beginDate		begin date
	 * @param endDate		  end date
	 * @return sum of service payments in the payment point for the period
	 */
	BigDecimal getPaymentPointServiceSumm(Stub<PaymentPoint> paymentPointStub, int statusCode, int serviceTypeCode, Date beginDate, Date endDate);

	/**
	 * Returns total sum of payments in cashbox for the period
	 *
	 * @param paymentPointStub payment point stub
	 * @param statusCode	   payment status code
	 * @param beginDate		begin date
	 * @param endDate		  end date
	 * @return total sum of payments in cashbox for the period
	 */
	BigDecimal getPaymentPointTotalSumm(Stub<PaymentPoint> paymentPointStub, int statusCode, Date beginDate, Date endDate);

	/**
	 * Returns sum of service payment in operation
	 *
	 * @param operationStub   operation stub
	 * @param serviceTypeCode service type code
	 * @return sum of service payment in operation
	 */
	BigDecimal getOperationServiceSumm(Stub<Operation> operationStub, int serviceTypeCode);
}
