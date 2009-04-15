package org.flexpay.payments.dao;

import org.flexpay.payments.persistence.Operation;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;

import java.util.List;
import java.util.Date;

public interface OperationDao extends GenericDao<Operation, Long> {

	/**
	 * List all operations which have been created between <code>beginDate</code> and <code>endDate</code>
	 * NOTE: operations with status DELETED are not included!
	 * 
	 * @param beginDate lower bound for operation creation date
	 * @param endDate higher bound for operation creation date
	 * @return list of operations
	 */
	List<Operation> listPaymentOperations(Date beginDate, Date endDate, Page<Operation> pager);
}
