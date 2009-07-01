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
	List<Document> listRegisteredPaymentDocumentsByServiceProvider(Long serviceProviderId, @NotNull Date begin, @NotNull Date end);

}
