package org.flexpay.payments.dao;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.payments.persistence.OperationActionLog;
import org.flexpay.payments.persistence.operation.sorter.OperationSorter;
import org.flexpay.payments.service.Roles;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

public interface OperationActionLogDaoExt {

    @Secured(Roles.OPERATION_READ)
    List<OperationActionLog> searchOperationActionLogs(OperationSorter operationSorter, @NotNull ArrayStack filters, Page<OperationActionLog> pager);

}
