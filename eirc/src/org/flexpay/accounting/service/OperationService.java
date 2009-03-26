package org.flexpay.accounting.service;

import org.flexpay.accounting.persistence.operations.Operation;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.annotation.Secured;

public interface OperationService {

	/**
	 * Read Operation object by Stub
	 *
	 * @param operationStub operation stub
	 * @return Operation object
	 */
	@Secured (Roles.OPERATION_READ)
	@Nullable
	public Operation read(@NotNull Stub<Operation> operationStub);

	/**
	 * Save operation
	 *
	 * @param operation Operation Object
	 */
	@Secured ({Roles.OPERATION_ADD, Roles.OPERATION_CHANGE})
	public void save(@NotNull Operation operation);

	/**
	 * Delete Operation object
	 *
	 * @param operationStub operation stub
	 */
	@Secured (Roles.OPERATION_DELETE)
	public void delete(@NotNull Stub<Operation> operationStub);

}
