package org.flexpay.payments.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.persistence.Operation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.math.BigDecimal;
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
	 * Save operation
	 *
	 * @param operation Operation Object
	 */
	@Secured ({Roles.OPERATION_ADD, Roles.OPERATION_CHANGE})
	void save(@NotNull Operation operation);

	/**
	 * Delete Operation object
	 *
	 * @param operationStub operation stub
	 */
	@Secured (Roles.OPERATION_DELETE)
	void delete(@NotNull Stub<Operation> operationStub);

	/**
	 * List all operations which have been created between <code>beginDate</code> and <code>endDate</code>
	 * NOTE: operations with status DELETED are not included!
	 *
	 * @param beginDate lower bound for operation creation date
	 * @param endDate higher bound for operation creation date
	 * @param pager Page
	 * @return list of operations
	 */
	@Secured(Roles.OPERATION_READ)
	List<Operation> listPaymentOperations(Date beginDate, Date endDate, Page<Operation> pager);

	/**
	 * List of all payment operations which has status REGISTERED inside time interval and organization
	 * @param organization organization
	 * @param beginDate lower bound for operation registration date
	 * @param endDate higher bound for operation registration date
	 * @return list of payment operations
	 */
	@Secured(Roles.OPERATION_READ)
	List<Operation> listReceivedPayments(Organization organization, Date beginDate, Date endDate);

	/**
	 * Returns list of operations which contains documents suitable to search criterias
	 * @param organization organization which registered operation
	 * @param serviceTypeId document service type id
	 * @param begin lower bound for document creation date
	 * @param end upper bound for document creation date
	 * @param minimalSumm minimal summ of document
	 * @param maximalSumm maximal summ of document
	 * @param pager pager (used for operations!)
	 * @return list of operations which contains documents suitable to search criterias
	 */
	@Secured(Roles.OPERATION_READ)
	List<Operation> searchDocuments(Organization organization, Long serviceTypeId, Date begin, Date end, BigDecimal minimalSumm, BigDecimal maximalSumm, Page<Operation> pager);

	/**
	 * Returns list of operations suitable to search criterias
	 * @param organization organization which registered operation
	 * @param begin lower bound for operation creation date
	 * @param end upper bound for operation creation date
	 * @param minimalSumm minimal operation summ
	 * @param maximalSumm maximal operation summ
	 * @param pager pager
	 * @return list of operations suitable to search criterias
	 */
	@Secured(Roles.OPERATION_READ)
	List<Operation> searchOperations(Organization organization, Date begin, Date end, BigDecimal minimalSumm, BigDecimal maximalSumm, Page<Operation> pager);
}
