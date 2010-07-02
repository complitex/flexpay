package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.persistence.DateRange;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Service;
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
	 * Returns list of documents with state REGISTERED and type CASH_PAYMENT which were created in time period for service
	 * provider
	 *
	 * @param serviceProviderId  Provider key
	 * @param registerOrganizationId Register organization key
	 * @param range			  Date range
	 * @param documentTypeCode   document type code
	 * @param documentStatusCode document status code
	 * @return list of documents with state REGISTERED and type CASH_PAYMENT which were created in time period
	 */
	List<Document> listPaymentDocumentsByServiceProvider(Long serviceProviderId, Long registerOrganizationId, DateRange range,
														 int documentTypeCode, int documentStatusCode);

	/**
	 * Returns list of services for documents with state REGISTERED and type CASH_PAYMENT which were created in time period
	 * for service provider
	 *
	 * @param serviceProviderId  Provider key
	 * @param registerOrganizationId Register organization key
	 * @param range			  Date range
	 * @param documentTypeCode   document type code
	 * @param documentStatusCode document status code
	 * @return list of documents with state REGISTERED and type CASH_PAYMENT which were created in time period
	 */
	List<Service> listPaymentsServicesByServiceProvider(Long serviceProviderId, Long registerOrganizationId, DateRange range,
														int documentTypeCode, int documentStatusCode);

	/**
	 * Returns list of payment points for documents with state REGISTERED and type CASH_PAYMENT which were created in time period
	 * for service provider
	 *
	 * @param serviceProviderId  Provider key
	 * @param registerOrganizationId Register organization key
	 * @param range			  Date range
	 * @param documentTypeCode   document type code
	 * @param documentStatusCode document status code
	 * @return list of documents with state REGISTERED and type CASH_PAYMENT which were created in time period
	 */
	List<PaymentPoint> listPaymentsPointsByServiceProvider(Long serviceProviderId, Long registerOrganizationId, DateRange range,
														int documentTypeCode, int documentStatusCode);

	/**
	 * Returns sum of payments for service in the cashbox for the period
	 *
	 * @param cashboxId	   cashbox id
	 * @param statusCode	  payment status code
	 * @param serviceTypeCode service type code
	 * @param beginDate	   begin date
	 * @param endDate		 end date
	 * @return sum of payments for service in the cashbox
	 */
	List findCashboxServiceSum(Long cashboxId, int statusCode, int serviceTypeCode, Date beginDate, Date endDate);

	/**
	 * Returns sum of payments in the cashbox for the period
	 *
	 * @param cashboxId  cashbox id
	 * @param statusCode payment status code
	 * @param beginDate  begin date
	 * @param endDate	end date
	 * @return sum of payments in the cashbox
	 */
	List findCashboxTotalSum(Long cashboxId, int statusCode, Date beginDate, Date endDate);

	/**
	 * Returns sum of service payments in the payment point for the period
	 *
	 * @param paymentPointId  payment point id
	 * @param statusCode	  payment status code
	 * @param serviceTypeCode service type code
	 * @param beginDate	   begin date
	 * @param endDate		 end date
	 * @return sum of service payments in the payment point for the period
	 */
	List findPaymentPointServiceSum(Long paymentPointId, int statusCode, int serviceTypeCode, Date beginDate, Date endDate);

	/**
	 * Returns total sum of payments in cashbox for the period
	 *
	 * @param paymentPointId payment point id
	 * @param statusCode	 payment status code
	 * @param beginDate	  begin date
	 * @param endDate		end date
	 * @return total sum of payments in cashbox for the period
	 */
	List findPaymentPointTotalSum(Long paymentPointId, int statusCode, Date beginDate, Date endDate);

	/**
	 * Returns sum of service payment in operation
	 *
	 * @param operationId	 operation id
	 * @param serviceTypeCode service type code
	 * @return sum of service payment in operation
	 */
	List findOperationServiceSum(Long operationId, int serviceTypeCode);
}
