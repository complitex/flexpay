package org.flexpay.payments.dao;

import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

public interface DocumentDaoExt {

	/**
	 * Returns list of operation documents which suits search criterias
	 *
	 * @param operation	 owner operation
	 * @param serviceTypeId documnent service type id
	 * @param minimalSumm   minimal document summ
	 * @param maximalSumm   maximal document summ
	 * @return list of operation documents which suits search criterias
	 */
	List<Document> searchDocuments(@NotNull Stub<Operation> operation, Long serviceTypeId, BigDecimal minimalSumm, BigDecimal maximalSumm);

}
