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
	 * List of all payment operations which has status REGISTERED inside time interval and cashbox
	 *
	 * @param cashboxId organization id
	 * @param beginDate lower bound for operation registration date
	 * @param endDate higher bound for operation registration date
	 * @param status operation status
	 * @return list of payment operations
	 */
	List<Operation> listPayments(Long cashboxId, Date beginDate, Date endDate, int status);

    /**
     * List of all payment operations which has status REGISTERED inside time interval, cashbox and register name
     *
     * @param cashboxId cashbox id
     * @param beginDate lower bound for operation registration date
     * @param endDate higher bound for operation registration date
     * @param status operation status
     * @param registerUserName registerUserName
     * 
     * @return list of payment operations
     */
	List<Operation> listPaymentsForOperator(Long cashboxId, Date beginDate, Date endDate, int status, String registerUserName);

    /**
     * List of all payment operations which has status REGISTERED inside time interval and payment point
     *
     * @param paymentPointId payment point id
     * @param beginDate lower bound for operation registration date
     * @param endDate higher bound for operation registration date
     * @param status operation status
     * @return list of payment operations
     */
    List<Operation> listPaymentsByPaymentPoint(Long paymentPointId, Date beginDate, Date endDate, int status);

	/**
     * List of all payment operations which has status REGISTERED inside time interval and payment collector
     *
     * @param paymentCollectorId payment collector id
     * @param beginDate lower bound for operation registration date
     * @param endDate higher bound for operation registration date
     * @param status operation status
     * @return list of payment operations
     */
    List<Operation> listPaymentsByPaymentCollector(Long paymentCollectorId, Date beginDate, Date endDate, int status);

	/**
	 * List of all payment operations which has status REGISTERED inside time interval and organization
	 *
	 * @param organizationId payment point id
	 * @param beginDate lower bound for operation registration date
	 * @param endDate higher bound for operation registration date
	 * @param status operation status
	 * @return list of payment operations
	 */
	List<Operation> listPaymentsByOrganization(Long organizationId, Date beginDate, Date endDate, int status);

    /**
     * List last operations which have been created between <code>beginDate</code> and <code>endDate</code>
     * NOTE: operations with status DELETED are not included!
     *
     * @param beginDate lower bound for operation creation date
     * @param endDate higher bound for operation creation date
     * @return list of operations
     */
    List<Operation> listLastPaymentOperations(Date beginDate, Date endDate);

}
