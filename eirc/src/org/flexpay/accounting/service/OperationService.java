package org.flexpay.accounting.service;

import org.flexpay.accounting.persistence.operations.Operation;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

public interface OperationService {

	/**
	 * Read Operation object by Stub
	 *
	 * @param operationStub operation stub
	 * @return Operation object
	 */
	public Operation read(@NotNull Stub<Operation> operationStub);

	/**
	 * Save operation
	 *
	 * @param operation Operation Object
	 */
	public void save(@NotNull Operation operation);

	/**
	 * Delete Operation object
	 *
	 * @param operationStub operation stub
	 */
	public void delete(@NotNull Stub<Operation> operationStub);

}
