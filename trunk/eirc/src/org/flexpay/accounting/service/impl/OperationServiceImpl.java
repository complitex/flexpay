package org.flexpay.accounting.service.impl;

import org.flexpay.accounting.service.OperationService;
import org.flexpay.accounting.persistence.Operation;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class OperationServiceImpl implements OperationService {
	/**
	 * Read Operation object by Stub
	 *
	 * @param operationStub operation stub
	 * @return Operation object
	 */
	@Transactional(readOnly = true)
	public Operation read(Stub<Operation> operationStub) {
		return null;
	}

	/**
	 * Save operation
	 *
	 * @param operation Operation Object
	 * @return saved Operation Object
	 */
	@Transactional(readOnly = false)
	@NotNull
	public Operation save(@NotNull Operation operation) {
		return null;
	}

	/**
	 * Create Operation Object
	 *
	 * @param operation Operation object to create
	 * @return created Operation object
	 */
	@Transactional(readOnly = false)
	@NotNull
	public Operation create(@NotNull Operation operation) {
		return null;
	}

	/**
	 * Delete Operation object
	 *
	 * @param operation Operation object to delete
	 */
	@Transactional(readOnly = false)
	public void delete(Operation operation) {
	}
}
