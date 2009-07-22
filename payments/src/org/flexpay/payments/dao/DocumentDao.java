package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.payments.persistence.Document;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public interface DocumentDao extends GenericDao<Document, Long> {

	/**
	 * Returns list of documents with state REGISTERED and type CASH_PAYMENT which were created in time period
	 *
	 * @param begin begin date
	 * @param end   end date
	 * @return list of documents with state REGISTERED and type CASH_PAYMENT which were created in time period
	 */
	List<Document> listRegisteredPaymentDocuments(@NotNull Date begin, @NotNull Date end);

    /**
	 * Returns list of documents with state REGISTERED and type CASH_PAYMENT which were created in time period
     * for service provider
	 *
     * @param serviceProviderId
	 * @param begin begin date
	 * @param end   end date
	 * @return list of documents with state REGISTERED and type CASH_PAYMENT which were created in time period
	 */
	List<Document> listRegisteredPaymentDocumentsByServiceProvider(Long serviceProviderId, Long registerOrganizationId, @NotNull Date begin, @NotNull Date end);

	/**
	 * Returns summ of payments for service in the cashbox for the period
	 * @param cashboxId cashbox id
	 * @param statusCode payment status code
	 * @param serviceTypeCode service type code
	 * @param beginDate begin date
	 * @param endDate end date
	 * @return summ of payments for service in the cashbox
	 */
	List findCashboxServiceSumm(Long cashboxId, int statusCode, int serviceTypeCode, Date beginDate, Date endDate);

	/**
	 * Returns summ of payments in the cashbox for the period
	 * @param cashboxId cashbox id
	 * @param statusCode payment status code
	 * @param beginDate begin date
	 * @param endDate end date
	 * @return summ of payments in the cashbox
	 */
	List findCashboxTotalSumm(Long cashboxId, int statusCode, Date beginDate, Date endDate);

	/**
	 * Returns summ of service payments in the payment point for the period
	 * @param paymentPointId payment point id
	 * @param statusCode payment status code
	 * @param serviceTypeCode service type code
	 * @param beginDate begin date
	 * @param endDate end date
	 * @return summ of service payments in the payment point for the period
	 */
	List findPaymentPointServiceSumm(Long paymentPointId, int statusCode, int serviceTypeCode, Date beginDate, Date endDate);

	/**
	 * Returns total summ of payments in cashbox for the period
	 * @param paymentPointId payment point id
	 * @param statusCode payment status code
	 * @param beginDate begin date
	 * @param endDate end date
	 * @return total summ of payments in cashbox for the period
	 */
	List findPaymentPointTotalSumm(Long paymentPointId, int statusCode, Date beginDate, Date endDate);

	/**
	 * Returns summ of service payment in operation
	 * @param operationId operation id
	 * @param serviceTypeCode service type code
	 * @return summ of service payment in operation
	 */
	List findOperationServiceSumm(Long operationId, int serviceTypeCode);
}
