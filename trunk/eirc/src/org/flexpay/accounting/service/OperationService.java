package org.flexpay.accounting.service;

import org.flexpay.accounting.persistence.Operation;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

public interface OperationService {

	/**
	 * Read Operation object by Stub
	 * @param operationStub operation stub
	 * @return Operation object
	 */
	public Operation read(Stub<Operation> operationStub);

	/**
	 * Save operation
	 * @param operation Operation Object
	 * @return saved Operation Object
	 */
	public Operation save(@NotNull Operation operation);

	/**
	 * Create Operation Object
	 * @param operation Operation object to create
	 * @return created Operation object
	 */
	public Operation create(@NotNull Operation operation);

	/**
	 * Delete Operation object
	 * @param operation Operation object to delete
	 */
	public void delete(Operation operation);
}
