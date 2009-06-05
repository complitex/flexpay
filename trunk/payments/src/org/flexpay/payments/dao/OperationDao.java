package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.payments.persistence.Operation;

import java.util.Date;
import java.util.List;

public interface OperationDao extends GenericDao<Operation, Long> {

	/**
	 * List all operations which have been created between <code>beginDate</code> and <code>endDate</code>
	 * NOTE: operations with status DELETED are not included!
	 *
	 * @param beginDate lower bound for operation creation date
	 * @param endDate higher bound for operation creation date
	 * @return list of operations
	 */
	List<Operation> listPaymentOperations(Date beginDate, Date endDate);

	/**
	 * List all operations which have been created between <code>beginDate</code> and <code>endDate</code>
	 * NOTE: operations with status DELETED are not included!
	 * 
	 * @param beginDate lower bound for operation creation date
	 * @param endDate higher bound for operation creation date
	 * @param pager Page
	 * @return list of operations
	 */
	List<Operation> listPaymentOperations(Date beginDate, Date endDate, Page<Operation> pager);

	/**
	 * List of all payment operations which has status REGISTERED inside time interval and organization
	 *
	 * @param organizationId organization id
	 * @param beginDate lower bound for operation registration date
	 * @param endDate higher bound for operation registration date
	 * @return list of payment operations
	 */
	List<Operation> listReceivedPayments(Long organizationId, Date beginDate, Date endDate);

}
