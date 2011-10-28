package org.flexpay.payments.dao;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.operation.sorter.OperationSorter;
import org.flexpay.payments.service.Roles;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.annotation.Secured;

import java.util.Date;
import java.util.List;

public interface OperationDaoExt {

    /**
     * Returns list of operations which contains documents suitable to search criterias
     *
     * @param operationSorter operation sorter
     * @param filters filters stack
     * @param pager pager (used for operations!)
     *
     * @return list of operations which contains documents suitable to search criterias
     */
    @Secured (Roles.OPERATION_READ)
    List<Operation> searchDocuments(OperationSorter operationSorter, @NotNull ArrayStack filters, Page<Operation> pager);

    /**
     * Returns list of operations suitable to search criterias
     *
     * @param operationSorter operation sorter
     * @param filters filters stack
     * @param pager pager
     * @return list of operations suitable to search criterias
     */
    @Secured(Roles.OPERATION_READ)
    List<Operation> searchOperations(OperationSorter operationSorter, @NotNull ArrayStack filters, Page<Operation> pager);

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

	Long getBlankOperationsCount(Long paymentCollectorId);

	void deleteAllBlankOperations();

	void deleteBlankOperations(Long paymentCollectorId);
}
