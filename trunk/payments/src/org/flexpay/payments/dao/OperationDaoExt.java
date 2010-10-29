package org.flexpay.payments.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.operation.sorter.OperationSorter;
import org.flexpay.payments.service.Roles;
import org.springframework.security.annotation.Secured;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OperationDaoExt {

	/**
	 * Returns list of operations which contains documents suitable to search criterias
	 *
     * @param operationSorter operation sorter
	 * @param cashbox Cashbox object
	 * @param serviceTypeId document service type id
	 * @param begin lower bound for document creation date
	 * @param end upper bound for document creation date
	 * @param minimalSum minimal sum of document
	 * @param maximalSum maximal sum of document
	 * @param pager pager (used for operations!)
	 * @return list of operations which contains documents suitable to search criterias
	 */
	@Secured (Roles.OPERATION_READ)
	List<Operation> searchDocuments(OperationSorter operationSorter, Stub<Cashbox> cashbox, Long serviceTypeId, Date begin, Date end,
									BigDecimal minimalSum, BigDecimal maximalSum, Page<Operation> pager);

    /**
     * Returns list of operations suitable to search criterias
     *
     * @param operationSorter operation sorter
     * @param tradingDayProcessId trading day process id
     * @param cashbox Cashbox object
     * @param begin lower bound for operation creation date
     * @param end upper bound for operation creation date
     * @param minimalSum minimal operation sum
     * @param maximalSum maximal operation sum
     * @param pager pager
     * @return list of operations suitable to search criterias
     */
    @Secured(Roles.OPERATION_READ)
    List<Operation> searchOperations(OperationSorter operationSorter, Long tradingDayProcessId, Stub<Cashbox> cashbox, Date begin, Date end, BigDecimal minimalSum,
                                     BigDecimal maximalSum, Page<Operation> pager);

    /**
     * List last operations which have been created between <code>beginDate</code> and <code>endDate</code>
     * NOTE: operations with status DELETED are not included!
     *
     * @param paymentPointId payment point id
     * @param beginDate lower bound for operation creation date
     * @param endDate higher bound for operation creation date
     * @return found operation
     */
    @Secured(Roles.OPERATION_READ)
    Operation getLastPaymentPointPaymentOperation(Long paymentPointId, Date beginDate, Date endDate);

    /**
     * List last operations which have been created between <code>beginDate</code> and <code>endDate</code>
     * NOTE: operations with status DELETED are not included!
     *
     * @param cashboxId cashbox id
     * @param beginDate lower bound for operation creation date
     * @param endDate higher bound for operation creation date
     * @return found operation
     */
    Operation getLastCashboxPaymentOperation(Long cashboxId, Date beginDate, Date endDate);

	Long getBlankOperationsCount();

	void deleteAllBlankOperations();
}
