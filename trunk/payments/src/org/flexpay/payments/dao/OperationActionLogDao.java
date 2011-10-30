package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.payments.persistence.OperationActionLog;

import java.util.Date;
import java.util.List;

public interface OperationActionLogDao extends GenericDao<OperationActionLog, Long> {

}
