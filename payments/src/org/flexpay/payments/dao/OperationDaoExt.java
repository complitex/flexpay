package org.flexpay.payments.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.service.Roles;
import org.springframework.security.annotation.Secured;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OperationDaoExt {

	/**
	 * Returns list of operations which contains documents suitable to search criterias
	 *
	 * @param cashbox Cashbox object
	 * @param serviceTypeId document service type id
	 * @param begin lower bound for document creation date
	 * @param end upper bound for document creation date
	 * @param minimalSumm minimal summ of document
	 * @param maximalSumm maximal summ of document
	 * @param pager pager (used for operations!)
	 * @return list of operations which contains documents suitable to search criterias
	 */
	@Secured (Roles.OPERATION_READ)
	List<Operation> searchDocuments(Stub<Cashbox> cashbox, Long serviceTypeId, Date begin, Date end,
									BigDecimal minimalSumm, BigDecimal maximalSumm, Page<Operation> pager);

    /**
     * Returns list of operations suitable to search criterias
     *
     * @param cashbox Cashbox object
     * @param begin lower bound for operation creation date
     * @param end upper bound for operation creation date
     * @param minimalSumm minimal operation summ
     * @param maximalSumm maximal operation summ
     * @param pager pager
     * @return list of operations suitable to search criterias
     */
    @Secured(Roles.OPERATION_READ)
    List<Operation> searchOperations(Stub<Cashbox> cashbox, Date begin, Date end, BigDecimal minimalSumm,
                                     BigDecimal maximalSumm, Page<Operation> pager);

	Long getBlankOperationsCount();

	void deleteAllBlankOperations();
}
