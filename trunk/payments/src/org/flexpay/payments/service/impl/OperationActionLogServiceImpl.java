package org.flexpay.payments.service.impl;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.payments.dao.OperationActionLogDao;
import org.flexpay.payments.dao.OperationActionLogDaoExt;
import org.flexpay.payments.persistence.OperationActionLog;
import org.flexpay.payments.persistence.operation.sorter.OperationSorter;
import org.flexpay.payments.service.OperationActionLogService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class OperationActionLogServiceImpl implements OperationActionLogService {

    private OperationActionLogDao operationActionLogDao;
    private OperationActionLogDaoExt operationActionLogDaoExt;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void create(@NotNull OperationActionLog operationActionLog) {
        operationActionLogDao.create(operationActionLog);
    }

    @Override
    public List<OperationActionLog> searchOperationActionLogs(OperationSorter operationSorter, @NotNull ArrayStack filters, Page<OperationActionLog> pager) {
        return operationActionLogDaoExt.searchOperationActionLogs(operationSorter, filters, pager);
    }

    @Required
    public void setOperationActionLogDao(OperationActionLogDao operationActionLogDao) {
        this.operationActionLogDao = operationActionLogDao;
    }

    @Required
    public void setOperationActionLogDaoExt(OperationActionLogDaoExt operationActionLogDaoExt) {
        this.operationActionLogDaoExt = operationActionLogDaoExt;
    }
}
