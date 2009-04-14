package org.flexpay.payments.service;

import org.flexpay.payments.persistence.Operation;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

import java.util.List;
import java.util.Date;

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
	 * @param beginDate lower bound for operation creation date
	 * @param endDate higher bound for operation creation date
	 * @return list of operations
	 */
	@Secured(Roles.OPERATION_READ)
	List<Operation> listPaymentOperations(Date beginDate, Date endDate, Page<Operation> pager);
}
