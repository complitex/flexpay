package org.flexpay.payments.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.payments.persistence.OperationActionLog;
import org.flexpay.payments.persistence.operation.sorter.OperationSorter;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface OperationActionLogService {

    @Secured(Roles.OPERATION_ADD)
    void create(@NotNull OperationActionLog operationActionLog);

    @Secured (Roles.OPERATION_READ)
    List<OperationActionLog> searchOperationActionLogs(OperationSorter operationSorter, @NotNull ArrayStack filters, Page<OperationActionLog> pager);

}
