package org.flexpay.payments.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.operation.sorter.OperationSorter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ws.server.endpoint.adapter.PayloadMethodEndpointAdapter;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface OperationService {

	/**
	 * Read Operation object by Stub
	 *
	 * @param operationStub operation stub
	 * @return Operation object
	 */
	@Secured (Roles.OPERATION_READ)
	@Nullable
	Operation read(@NotNull Stub<Operation> operationStub);

    /**
     * Read operations collection by theirs ids
     *
     * @param operationIds Apartment ids
     * @param preserveOrder Whether to preserve order of objects
     * @return Found operations
     */
    @Secured ({Roles.OPERATION_READ})
    @NotNull
    List<Operation> readFull(@NotNull Collection<Long> operationIds, boolean preserveOrder);

	/**
	 * Save operation
	 *
	 * @param operation Operation Object
	 */
	@Secured (Roles.OPERATION_ADD)
	void create(@NotNull Operation operation);

	/**
	 * Save operation
	 *
	 * @param operation Operation Object
	 */
	@Secured (Roles.OPERATION_CHANGE)
	void update(@NotNull Operation operation);

	/**
	 * Delete Operation object
	 *
	 * @param operationStub operation stub
	 */
	@Secured (Roles.OPERATION_DELETE)
	void delete(@NotNull Stub<Operation> operationStub);

	/**
	 * List all operations which have been created between <code>beginDate</code> and <code>endDate</code> NOTE: operations
	 * with status DELETED are not included!
	 *
	 * @param beginDate lower bound for operation creation date
	 * @param endDate   higher bound for operation creation date
	 * @param pager	 Page
	 * @return list of operations
	 */
	@Secured (Roles.OPERATION_READ)
	List<Operation> listPaymentOperations(Date beginDate, Date endDate, Page<Operation> pager);

	/**
	 * List all operations which have been created between <code>beginDate</code> and <code>endDate</code> NOTE: operations
	 * with status DELETED are not included!
	 *
	 * @param beginDate lower bound for operation creation date
	 * @param endDate   higher bound for operation creation date
	 * @return list of operations
	 */
	@Secured (Roles.OPERATION_READ)
	List<Operation> listPaymentOperations(Date beginDate, Date endDate);

	/**
	 * List last operations which have been created between <code>beginDate</code> and <code>endDate</code> NOTE:
	 * operations with status DELETED are not included!
	 *
	 * @param beginDate lower bound for operation creation date
	 * @param endDate   higher bound for operation creation date
	 * @return list of operations
	 */
	@Secured (Roles.OPERATION_READ)
	List<Operation> listLastPaymentOperations(Date beginDate, Date endDate);

	/**
	 * List last operations which have been created between <code>beginDate</code> and <code>endDate</code> NOTE:
	 * operations with status DELETED are not included!
	 *
	 * @param paymentPoint payment point
	 * @param beginDate	lower bound for operation creation date
	 * @param endDate	  higher bound for operation creation date
	 * @return list of operations
	 */
	@Secured (Roles.OPERATION_READ)
	Operation getLastPaymentOperationForPaymentPoint(Stub<PaymentPoint> paymentPoint, Date beginDate, Date endDate);

	/**
	 * List last operations which have been created between <code>beginDate</code> and <code>endDate</code> NOTE:
	 * operations with status DELETED are not included!
	 *
	 * @param cashbox   cashbox
	 * @param beginDate lower bound for operation creation date
	 * @param endDate   higher bound for operation creation date
	 * @return list of operations
	 */
	@Secured (Roles.OPERATION_READ)
	Operation getLastPaymentOperationsForCashbox(Stub<Cashbox> cashbox, Date beginDate, Date endDate);

	/**
	 * List of all payment operations which has status REGISTERED inside time interval and organization
	 *
	 * @param cashbox   cashbox
	 * @param beginDate lower bound for operation registration date
	 * @param endDate   higher bound for operation registration date
	 * @return list of payment operations
	 */
	@Secured (Roles.OPERATION_READ)
	List<Operation> listReceivedPaymentsForCashbox(Stub<Cashbox> cashbox, Date beginDate, Date endDate);

	/**
	 * List of all payment operations which has status RETURNED inside time interval and cashbox
	 *
	 * @param cashbox   cashbox
	 * @param beginDate lower bound for operation registration date
	 * @param endDate   higher bound for operation registration date
	 * @return list of payment operations
	 */
	@Secured (Roles.OPERATION_READ)
	List<Operation> listReturnedPaymentsForCashbox(Stub<Cashbox> cashbox, Date beginDate, Date endDate);

	@Secured (Roles.OPERATION_READ)
	List<Operation> listReceivedPaymentsForOperator(Stub<Cashbox> cashbox, Date beginDate, Date endDate, String registerUserName);

	@Secured (Roles.OPERATION_READ)
	List<Operation> listReturnedPaymentsForOperator(Stub<Cashbox> cashbox, Date beginDate, Date endDate, String registerUserName);

    /**
     * List of all payment operations which has status REGISTERED inside time interval and organization
     *
     * @param stub Payment point stub
     * @param beginDate lower bound for operation registration date
     * @param endDate   higher bound for operation registration date
     * @return list of payment operations
     */
    @Secured (Roles.OPERATION_READ)
    List<Operation> listReceivedPaymentsForPaymentCollector(Stub<PaymentCollector> stub, Date beginDate, Date endDate);

	/**
     * List of all payment operations which has status REGISTERED inside time interval and organization
     *
     * @param stub Payment point stub
     * @param beginDate lower bound for operation registration date
     * @param endDate   higher bound for operation registration date
     * @return list of payment operations
     */
    @Secured (Roles.OPERATION_READ)
    List<Operation> listReceivedPaymentsForPaymentPoint(Stub<PaymentPoint> stub, Date beginDate, Date endDate);

	/**
	 * List of all payment operations which has status REGISTERED inside time interval and organization
	 *
	 * @param organization organization
	 * @param beginDate	lower bound for operation registration date
	 * @param endDate	  higher bound for operation registration date
	 * @return list of payment operations
	 */
	@Secured (Roles.OPERATION_READ)
	List<Operation> listReceivedPaymentsForOrganization(Stub<Organization> organization, Date beginDate, Date endDate);

    /**
     * Returns list of operations which contains documents suitable to search criterias
     *
     * @param operationSorter operation sorter
     * @param filters filters stack
     * @param pager		 pager (used for operations!)
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
	 * @param pager	   pager
	 * @return list of operations suitable to search criterias
	 */
	@Secured (Roles.OPERATION_READ)
	List<Operation> searchOperations(OperationSorter operationSorter, @NotNull ArrayStack filters, Page<Operation> pager);

	/**
	 * Creates new operation with no data and BLANK state
	 *
	 * @param creator name of user who created operation
	 * @param cashboxStub cashbox
	 * @return new operation instance
	 * @throws FlexPayException if creation failed
	 */
	@Secured (Roles.OPERATION_ADD)
	Operation createBlankOperation(String creator, Stub<Cashbox> cashboxStub) throws FlexPayException;

	@Secured (Roles.OPERATION_READ)
	Long getBlankOperationsCount() throws FlexPayException;

	@Secured (Roles.OPERATION_READ)
	Long getBlankOperationsCount(Stub<PaymentCollector> stub) throws FlexPayException;

	@Secured (Roles.OPERATION_DELETE)
	void deleteAllBlankOperations() throws FlexPayException;

	@Secured (Roles.OPERATION_DELETE)
	void deleteBlankOperations(Stub<PaymentCollector> stub) throws FlexPayException;

}
