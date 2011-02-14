package org.flexpay.payments.dao;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DocumentDaoExt {

	/**
	 * Returns list of operation documents which suits search criterias
	 *
	 * @param operation	 owner operation
     * @param filters filters stack
     *
	 * @return list of operation documents which suits search criterias
	 */
	List<Document> searchDocuments(@NotNull Stub<Operation> operation, @NotNull ArrayStack filters);

}
