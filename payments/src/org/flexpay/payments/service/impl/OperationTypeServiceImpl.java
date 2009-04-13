package org.flexpay.payments.service.impl;

import org.flexpay.payments.service.OperationTypeService;
import org.flexpay.payments.persistence.OperationType;
import org.flexpay.payments.dao.OperationTypeDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.exception.FlexPayException;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

@Transactional (readOnly = true)
public class OperationTypeServiceImpl implements OperationTypeService {

	private OperationTypeDao operationTypeDao;

	/**
	 * Read full operation type details
	 *
	 * @param stub Operation type stub
	 * @return OperationType if found, or <code>null</code> otherwise
	 */
	public OperationType read(@NotNull Stub<OperationType> stub) {
		return operationTypeDao.readFull(stub.getId());
	}

	/**
	 * Read full operation type details by code
	 *
	 * @param code Operation type code
	 * @return OperationType if found, or <code>null</code> otherwise
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if lookup by code fails
	 */
	@NotNull
	public OperationType read(int code) throws FlexPayException {

		List<OperationType> types = operationTypeDao.findByCode(code);
		if (types.isEmpty()) {
			throw new FlexPayException("Type not found #" + code);
		}
		return types.get(0);
	}

	@Required
	public void setOperationTypeDao(OperationTypeDao operationTypeDao) {
		this.operationTypeDao = operationTypeDao;
	}
}
