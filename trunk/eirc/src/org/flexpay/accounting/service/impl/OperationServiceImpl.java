package org.flexpay.accounting.service.impl;

import org.flexpay.accounting.dao.OperationDAO;
import org.flexpay.accounting.persistence.operations.Operation;
import org.flexpay.accounting.service.OperationService;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class OperationServiceImpl implements OperationService {

	@NonNls
	private Logger log = LoggerFactory.getLogger(getClass());

	private OperationDAO operationDao;

	/**
	 * Read Operation object by Stub
	 *
	 * @param operationStub operation stub
	 * @return Operation object
	 */
	public Operation read(@NotNull Stub<Operation> operationStub) {
		return operationDao.readFull(operationStub.getId());
	}

	/**
	 * Save operation
	 *
	 * @param operation Operation Object
	 */
	@Transactional(readOnly = false)
	public void save(@NotNull Operation operation) {
		if (operation.isNew()) {
			operation.setId(null);
			operationDao.create(operation);
		} else {
			operationDao.update(operation);
		}
	}

	/**
	 * Delete Operation object
	 *
	 * @param operationStub operation stub
	 */
	@Transactional(readOnly = false)
	public void delete(@NotNull Stub<Operation> operationStub) {
		Operation operation = operationDao.read(operationStub.getId());

		if (operation == null) {
			log.debug("Can't find operation with id {}", operationStub.getId());
			return;
		}

		operationDao.delete(operation);
	}

	public void setOperationDao(OperationDAO operationDao) {
		this.operationDao = operationDao;
	}

}
